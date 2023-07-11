package com.elikill58.ultimatehammer.common.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.elikill58.ultimatehammer.api.GameMode;
import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockData;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.utils.ItemUtils;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.common.UltimateToolType;
import com.elikill58.ultimatehammer.common.tools.axe.DetectedTree;
import com.elikill58.ultimatehammer.common.tools.axe.Tree;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class AxeManager extends UltimateToolType implements Listeners {
	
	public AxeManager() {
		super("axe");
	}
	
	@EventListener
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		getToolForHand(p).ifPresent(tool -> {
			Block b = e.getBlock();
			if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
				return;
			fellTree(tool, b, p, p.getItemInHand());
		});
	}

	public static boolean fellTree(UltimateTool tool, Block block, Player player, ItemStack axe) {
		DetectedTree detectedTree = detectTree(block, player, axe, ItemUtils.getDurabilityForItemAsTool(tool, player, axe)); /*(testTree) -> {
			int total = toList(testTree.trunk).size();
			int dura = axe.getDurability(), max = axe.getType().getMaxDurability();
			int damage = ItemUtils.getAmountOfDamage(tool, player, axe, total);
			if(damage > 0) {
				testTree.damageApplied += damage;
				return max <= dura + testTree.damageApplied; // false if should continue
			}
			return null; // continue
		});*/
		if (detectedTree == null)
			return false;
		int total = toList(detectedTree.trunk).size();
		ItemUtils.damage(tool, player, axe, player.getInventory().getHeldItemSlot(), total);
		int lower = block.getY();
		for (Block b : toList(detectedTree.trunk)) {
			if (b.getY() < lower)
				lower = b.getY();
		}
		// now the blocks
		ArrayList<Integer> distances = new ArrayList<>(detectedTree.trunk.keySet());
		Collections.sort(distances);
		for (int i : distances) {
			for (Block b : detectedTree.trunk.get(i)) {
				if (total <= 0)
					break;
				for (Block leaf : toList(getBlocks(detectedTree.tree.leaves, b, 6, detectedTree.tree.diagonalLeave))) {
					if(!leaf.getType().equals(Materials.AIR) && !tool.usedBreak(player, leaf))
						leaf.breakNaturally(axe);
				}
				if(!b.getType().equals(Materials.AIR) && !tool.usedBreak(player, b)) {
					b.breakNaturally(axe);
					total--;
				}
			}
		}
		return true;
	}

	public static DetectedTree detectTree(Block block, Player player, ItemStack axe,
			int dura) {
		if (player != null && player.getGameMode() == GameMode.SPECTATOR)
			return null;
		Material material = block.getType();
		for (Tree tree : Tree.getTreeForServer()) {
			if (!tree.trunk.contains(material)) {
				continue;
			}
			HashMap<Integer, ArrayList<Block>> blocks = getBlocks(tree.trunk, block, 256, true);
			if(blocks.isEmpty())
				continue;
			int minY = block.getY();
			for (int i : blocks.keySet()) {
				for (Block b : blocks.get(i)) {
					minY = Math.min(minY, b.getY());
				}
			}
			return new DetectedTree(tree, blocks);
		}
		return null;
	}

	private static HashMap<Integer, ArrayList<Block>> getBlocks(Collection<Material> materialTypes, Block startingBlock, int maxDistance, boolean diagonal) {
		// layer zero
		HashMap<Integer, ArrayList<Block>> results = new HashMap<>();
		ArrayList<Block> zero = new ArrayList<>();
		if (materialTypes.contains(startingBlock.getType())) {
			zero.add(startingBlock);
		}
		results.put(0, zero);
		// all the other layers
		for (int i = 0; i < maxDistance; i++) {
			ArrayList<Block> layer = new ArrayList<>();
			ArrayList<Block> lastLayer = new ArrayList<>(results.get(i));
			if (i == 0 && lastLayer.isEmpty()) {
				lastLayer.add(startingBlock);
			}
			for (Block block : lastLayer) {
				if (diagonal) {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							for (int z = -1; z <= 1; z++) {
								if (x == 0 && y == 0 && z == 0)
									continue;// same block
								checkBlock(block, x, y, z, materialTypes, layer, lastLayer, i, results);
							}
						}
					}
				} else {
					for (int j = 0; j < 6; j++) {
						int x = 0, y = 0, z = 0;
						switch (j) {
						case 0:
							x = -1;
							break;
						case 1:
							x = 1;
							break;
						case 2:
							y = -1;
							break;
						case 3:
							y = 1;
							break;
						case 4:
							z = -1;
							break;
						case 5:
							z = 1;
							break;
						default:
							break;
						}
						checkBlock(block, x, y, z, materialTypes, layer, lastLayer, i, results);
					}
				}
			}
			if (layer.isEmpty())
				break;
			results.put(i + 1, layer);
		}
		return results;
	}
	
	private static void checkBlock(Block block, int x, int y, int z, Collection<Material> materialTypes, ArrayList<Block> layer, ArrayList<Block> lastLayer, int i, HashMap<Integer, ArrayList<Block>> results) {
		Block newBlock = block.getRelative(x, y, z);
		if (lastLayer.contains(newBlock))
			return;// if the new block is on the same layer, ignore
		if (i > 0 && results.get(i - 1).contains(newBlock))
			return;// if the new block is on the previous layer, ignore
		if (!materialTypes.contains(newBlock.getType()))
			return;
		if (layer.contains(newBlock))
			return;// if the new block is on the next layer, but already processed, ignore
		BlockData bd = newBlock.getBlockData();
		if(bd.isLeavesPersistent() || (bd.getLeavesDistance() <= block.getBlockData().getLeavesDistance() && block.getBlockData().getLeavesDistance() != -1))
			return;
		layer.add(newBlock);
	}

	private static ArrayList<Block> toList(HashMap<Integer, ArrayList<Block>> blocks) {
		ArrayList<Block> list = new ArrayList<>();
		blocks.values().forEach(list::addAll);
		return list;
	}
}

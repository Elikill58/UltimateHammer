package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.tools.axe.DetectedTree;
import com.elikill58.ultimatehammer.tools.axe.Tree;
import com.elikill58.ultimatehammer.utils.Utils;

@SuppressWarnings("deprecation")
public class AxeManager extends UltimateTool implements Listener {

	public AxeManager(UltimateHammer pl) {
		super(pl, "axe");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if (!isEnabled() || e.isCancelled())
			return;
		Player p = e.getPlayer();
		ItemStack item = Utils.getItemInHand(p);
		if (!isItem(item))
			return;
		Block b = e.getBlock();
		if (WorldRegionBypass.cannotBuild(p, this, b.getLocation()))
			return;
		if (!fellTree(b, p, item))
			e.setCancelled(true);
	}

	public static boolean fellTree(Block block, Player player, ItemStack axe) {
		ItemMeta meta = axe.hasItemMeta() ? axe.getItemMeta() : null;
		boolean unbreakable = meta != null && meta.isUnbreakable();
		DetectedTree detectedTree = detectTree(block, player, axe, (testTree) -> {
			int durability = axe.getType().getMaxDurability() - axe.getDurability();
			int total = toList(testTree.trunk).size();
			int durabilityCost = total;
			if (unbreakable)
				durabilityCost = 0;
			else {
				durabilityCost /= (axe.getEnchantmentLevel(Enchantment.DURABILITY) + 1);
				if (durabilityCost < 1)
					durabilityCost++;
			}
			if (axe.getType().getMaxDurability() == 0)
				durabilityCost = 0;// there is no durability
			if (player.getGameMode() == GameMode.CREATIVE)
				durabilityCost = 0;// Don't cost durability
			return durabilityCost > durability ? null : true;
		});
		if (detectedTree == null)
			return false;
		int durability = axe.getType().getMaxDurability() - axe.getDurability();
		int total = toList(detectedTree.trunk).size();
		int durabilityCost = total;
		if (unbreakable)
			durabilityCost = 0;
		else {
			durabilityCost /= (axe.getEnchantmentLevel(Enchantment.DURABILITY) + 1);
			if (durabilityCost < 1)
				durabilityCost++;
		}
		if (axe.getType().getMaxDurability() == 0)
			durabilityCost = 0;// there is no durability
		if (player.getGameMode() == GameMode.CREATIVE)
			durabilityCost = 0;// Don't cost durability
		else {
			if (axe.getType().getMaxDurability() > 0) {
				axe.setDurability((short) (axe.getDurability() + durabilityCost));
				if (durability == durabilityCost) {
					axe.setAmount(0);
				}
			}
		}
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
					leaf.breakNaturally(axe);
				}
				b.breakNaturally(axe);
				total--;
			}
		}
		return true;
	}

	public static DetectedTree detectTree(Block block, Player player, ItemStack axe,
			Function<DetectedTree, Boolean> checkFunc) {
		if (player != null && player.getGameMode() == GameMode.SPECTATOR)
			return null;
		Material material = block.getType();
		for (Tree tree : Tree.values()) {
			if (!tree.trunk.contains(material)) {
				continue;
			}
			HashMap<Integer, ArrayList<Block>> blocks = getBlocks(tree.trunk, block, 256, true);
			int minY = block.getY();
			for (int i : blocks.keySet()) {
				for (Block b : blocks.get(i)) {
					minY = Math.min(minY, b.getY());
				}
			}
			ArrayList<Integer> distances = new ArrayList<>(blocks.keySet());
			Collections.sort(distances);
			HashMap<Integer, ArrayList<Block>> allLeaves = new HashMap<>();
			ArrayList<Block> everything = new ArrayList<>();
			everything.addAll(toList(blocks));
			everything.addAll(toList(allLeaves));
			DetectedTree detected = new DetectedTree(tree, blocks, allLeaves);
			Boolean result = checkFunc.apply(detected);
			if (result == null)
				continue;
			if (Objects.equals(result, false))
				continue;
			return detected;
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
		if (newBlock.getBlockData() instanceof Leaves) {
			Leaves newLeaf = (Leaves) newBlock.getBlockData();
			if (newLeaf.isPersistent())
				return;
			if (block.getBlockData() instanceof Leaves) {
				if (newLeaf.getDistance() <= ((Leaves) block.getBlockData()).getDistance())
					return;
			}
		}
		layer.add(newBlock);
	}

	private static ArrayList<Block> toList(HashMap<Integer, ArrayList<Block>> blocks) {
		ArrayList<Block> list = new ArrayList<>();
		blocks.values().forEach(list::addAll);
		return list;
	}
}

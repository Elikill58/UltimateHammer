package com.elikill58.ultimatehammer.common.tools;

import java.util.Arrays;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockData;
import com.elikill58.ultimatehammer.api.block.BlockFace;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.events.entity.ItemSpawnEvent;
import com.elikill58.ultimatehammer.api.events.player.PlayerInteractEvent;
import com.elikill58.ultimatehammer.api.events.player.PlayerInteractEvent.Action;
import com.elikill58.ultimatehammer.api.inventory.PlayerInventory;
import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.utils.ItemUtils;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.common.UltimateToolType;
import com.elikill58.ultimatehammer.common.tools.hoe.Plantable;
import com.elikill58.ultimatehammer.common.tools.hoe.Plantable.PlantableType;
import com.elikill58.ultimatehammer.common.tools.hoe.pickup.LocationActions;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Scheduler;
import com.elikill58.ultimatehammer.universal.Version;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class HoeManager extends UltimateToolType implements Listeners {

	public static final String KEY = "hoe";

	public HoeManager() {
		super(KEY);
		add(PlantableType.BASIC, Materials.SEEDS, Materials.CROPS, (byte) 7);

		Version v = Version.getVersion();
		//if(v.isNewerOrEquals(Version.V1_17)) {
			add(PlantableType.BASIC, Materials.POTATO_ITEM, Materials.POTATOES, (byte) 7);
			add(PlantableType.BASIC, Materials.CARROT_ITEM, Materials.CARROTS, (byte) 7);
		/*} else {
			add(PlantableType.BASIC, Materials.POTATO_ITEM, Materials.POTATO, (byte) 7);
			add(PlantableType.BASIC, Materials.CARROT_ITEM, Materials.CARROT, (byte) 7);
		}*/

		add(PlantableType.NETHER, Materials.NETHER_WART_ITEM, Materials.NETHER_WART, (byte) 3);
		if (v.isNewerOrEquals(Version.V1_9))
			add(PlantableType.BASIC, Materials.BEETROOT_SEEDS, Materials.BEETROOTS, (byte) 7);
	}

	private void add(PlantableType type, Material invItem, Material nextItem, byte neededDataToGet) {
		type.addPlantage(new Plantable(invItem, nextItem, neededDataToGet));
	}

	private Block getGoodBlockForPlant(Block b) {
		return Arrays.asList(Materials.DIRT, Materials.GRASS, Materials.SOIL, Materials.SOUL_SAND).contains(b.getType()) ? b.getRelative(BlockFace.UP) : b;
	}
	
	@EventListener
	public void onInteract(PlayerInteractEvent e) {
		if (e.getBlock() == null || e.getAction().equals(Action.PHYSICAL))
			return;
		Player p = e.getPlayer();
		getToolForHand(p).ifPresent((tool) -> {
			Block b = getGoodBlockForPlant(e.getBlock());
			if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
				return;
			e.setCancelled(true);
			managePlants(p, b, tool, e.getAction().name().contains("RIGHT"));
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		getToolForHand(p).ifPresent((tool) -> {
			Block b = getGoodBlockForPlant(e.getBlock());
			if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
				return;
			e.setCancelled(true);
			p.sendMessage("Run when break");
			managePlants(p, b, tool, true);
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onSpawn(ItemSpawnEvent e) {
		if (LocationActions.doAutoActions(e.getItem(), e.getLocation())) {
			e.setCancelled(true);
		}
	}

	public void managePlants(Player p, Block baseBlock, UltimateTool tool, boolean keepEmpty) {
		Block baseDirt = baseBlock.getRelative(BlockFace.DOWN);
		PlantableType plantableType = PlantableType.getPlantageType(baseDirt.getType());
		if (plantableType == null) {
			Adapter.getAdapter().debug("Can't find valid plantable type for " + baseDirt.getType().getId());
			return;
		}
		ItemStack inHand = p.getItemInHand();
		int slot = p.getInventory().getHeldItemSlot();
		World w = baseBlock.getWorld();
		int count = 0, size = (tool.getHoeSize() - 1) / 2;
		int x = baseBlock.getX(), y = baseBlock.getY(), z = baseBlock.getZ();
		Adapter.getAdapter().debug("Bases: " + size + " > " + x + ", " + y + ", " + z);
		for (int xx = (x - size); xx <= (x + size); xx++) {
			for (int zz = (z - size); zz <= (z + size); zz++) {
				Block checking = w.getBlockAt(xx, y, zz); // get new block
				if (WorldRegionBypass.cannotBuild(p, tool, checking.getLocation())) // not allowed to build
					continue;
				Block dirt = checking.getRelative(BlockFace.DOWN); // get dirt below
				if (!plantableType.getMaterial().contains(dirt.getType())) // near to invalid block (sand, gravel etc)
					continue;
				if (dirt.getType().equals(Materials.GRASS) || dirt.getType().equals(Materials.DIRT)) {
					// not soil but set it to
					if (!checking.getType().isTransparent()) // don't change type when upper it's not like grass
						continue;
					dirt.setType(Materials.SOIL);
					count++;
					Block up = dirt.getLocation().add(0, 1, 0).getBlock();
					if (up.getType().isTransparent()) // upper is not full block
						up.setType(Materials.AIR);
					continue;
				} else if (dirt.getType().equals(Materials.SOIL)
						&& dirt.getBlockData().getHumidity() != dirt.getBlockData().getMaximumHumidity()) {
					continue;
				}
				Material checkingMaterial = checking.getType();
				boolean needNewPlant = checkingMaterial.equals(Materials.AIR);
				Plantable plant = PlantableType.getPlantage(checkingMaterial);
				if (plant == null) {
					for (ItemStack temp : p.getInventory().getContents()) {
						if (temp != null) {
							plant = plantableType.getPlantageHasInventoryItem(temp.getType());
							if (plant != null && (needNewPlant || plant.getNextItem() == checking.getType()))
								break;
						}
					}
				}
				if (plant == null) {
					plant = PlantableType.getPlantage(checking.getType());
					if (plant == null)
						continue;
				}
				if (plant.getNextItem() != checking.getType()) {
					Plantable pl = PlantableType.getPlantage(checking.getType());
					if (pl != null)
						plant = pl;
				}
				BlockData data = checking.getBlockData();
				Material value = plant.getNextItem();
				if ((value.equals(checking.getType()) && data
						.getAge() == (data.getMaximumAge() == -1 ? plant.getNeededDataToGet() : data.getMaximumAge()))
						|| (needNewPlant && keepEmpty && checking.getType().equals(Materials.AIR))) {
					if (WorldRegionBypass.cannotBuild(p, tool, checking.getLocation()))
						continue;
					boolean notAllowed = Adapter.getAdapter().callBreakEvent(checking, p);
					if(notAllowed)
						continue;
					if (!tool.usedBreak(p, checking)) {
						count++;
						boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
						LocationActions.add(checking.getLocation(), p, !isRemoved);
						checking.breakNaturally(inHand);
						checking.setType(plant.getNextItem());
					}
				}
			}
		}
		if (count > 0 && !inHand.isUnbreakable()) {
			count /= (inHand.getEnchantLevel(Enchantment.UNBREAKING) + 1);
			if (count < 1)
				count = 1;
			int counterDamage = (int) (count / tool.getConfigSection().getDouble("dura-reduction", 1));
			if (counterDamage == 0)
				counterDamage = 1;
			ItemUtils.damage(tool, p, inHand, slot, counterDamage);
		}
	}

	/**
	 * Try to remove the first item
	 * 
	 * @param p the player which will lose the item
	 * @param m the type of item to remove
	 * @return true if the item is well removed
	 */
	public static boolean tryToRemoveFirstItem(Player p, Material m) {
		PlayerInventory inv = p.getInventory();
		for (int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack item = inv.get(slot);
			if (item != null && item.getType().equals(m)) {
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() == 0)
					inv.set(slot, null);
				else
					inv.set(slot, item);
				return true;
			}
		}
		return false;
	}
}

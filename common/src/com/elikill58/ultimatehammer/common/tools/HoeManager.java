package com.elikill58.ultimatehammer.common.tools;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.events.entity.ItemSpawnEvent;
import com.elikill58.ultimatehammer.api.events.player.PlayerInteractEvent;
import com.elikill58.ultimatehammer.api.events.player.PlayerInteractEvent.Action;
import com.elikill58.ultimatehammer.api.inventory.PlayerInventory;
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
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class HoeManager extends UltimateToolType implements Listeners {

	public static final String KEY = "hoe";

	public HoeManager() {
		super(KEY);
		add(PlantableType.BASIC, Materials.SEED, Materials.CROPS, (byte) 7);
		add(PlantableType.BASIC, Materials.POTATO_ITEM, Materials.POTATO, (byte) 7);
		add(PlantableType.BASIC, Materials.CARROT_ITEM, Materials.CARROT, (byte) 7);
		add(PlantableType.NETHER, Materials.NETHER_WART_ITEM, Materials.NETHER_WART, (byte) 3);
	}
	
	private void add(PlantableType type, Material invItem, Material nextItem, byte neededDataToGet) {
		type.addPlantage(new Plantable(invItem, nextItem, neededDataToGet));
	}

	@EventListener
	public void onInteract(PlayerInteractEvent e) {
		if (e.getBlock() == null || e.getAction().equals(Action.PHYSICAL))
			return;
		Player p = e.getPlayer();
		Block baseBlock = e.getBlock();
		ItemStack inHand = p.getItemInHand();
		getToolForHand(p, getKey()).forEach((tool) -> {
			if (WorldRegionBypass.cannotBuild(p, tool, baseBlock.getLocation()))
				return;
			int slot = p.getInventory().getHeldItemSlot();
			e.setCancelled(true);
			manageAllPlant(p, baseBlock.getLocation().add(0, 1, 0).getBlock(), tool, tool.getConfigSection().getInt("hoe.size", 3), e.getAction().name().contains("RIGHT"), false);
			if(inHand != null) {
				if(ItemUtils.getUseAmount(p, inHand) <= 0)
					p.getInventory().set(slot, null);
				else
					p.getInventory().set(slot, inHand);
			}
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block baseBlock = e.getBlock();
		getToolForHand(p, getKey()).forEach((tool) -> {
			Block b = e.getBlock();
			if(WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
				return;
			e.setCancelled(true);
			manageAllPlant(p, baseBlock, tool, tool.getConfigSection().getInt("hoe.size", 3), true, true);
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onSpawn(ItemSpawnEvent e) {
		if (LocationActions.doAutoActions(e.getItem(), e.getLocation())) {
			e.setCancelled(true);
		}
	}
	
	public static int manageAllPlant(Player p, Block baseBlock, UltimateTool tool, int size, boolean keepEmpty, boolean fromBreak) {
		if(baseBlock.getType() == Materials.AIR) {
			baseBlock = baseBlock.getLocation().sub(0, 1, 0).getBlock();
		}
		Material m = baseBlock.getType();
		PlantableType plantableType = PlantableType.getPlantageType(m);
		if (plantableType == null) {
			plantableType = PlantableType.getPlantageType(baseBlock.getLocation().clone().sub(0, 1, 0).getBlock().getType());
			if(plantableType == null) {
				Adapter.getAdapter().getLogger().warn("No plantable type founded for " + baseBlock.getLocation().clone().sub(0, 1, 0).getBlock().getType().getId());
				return 0;
			}
		}
		ItemStack inHand = p.getItemInHand();
		int slot = p.getInventory().getHeldItemSlot();
		World w = baseBlock.getWorld();
		int count = 0;
		int amount = (size - 1) / 2;
		int x = baseBlock.getX(), y = baseBlock.getY(), z = baseBlock.getZ();
		for (int xx = (x - amount); xx <= (x + amount); xx++) {
			for (int zz = (z - amount); zz <= (z + amount); zz++) {
				Block b = w.getBlockAt(xx, y, zz);
				if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
					continue;
				Material blockMaterial = b.getType();
				Block dirt = plantableType.getMaterial().contains(blockMaterial) ? b : w.getBlockAt(xx, y - 1, zz);
				if (dirt.getType().equals(Materials.GRASS) || dirt.getType().equals(Materials.DIRT)) {
					dirt.setType(Materials.SOIL);
					Block up = dirt.getLocation().add(0, 1, 0).getBlock();
					if(up.getType().getId().contains("GRASS"))
						up.setType(Materials.AIR);
					continue;
				} else if(dirt.getType().equals(Materials.SOIL) && dirt.getData() < 7)
					continue;
				if (!plantableType.getMaterial().contains(dirt.getType())) {
					continue;
				}
				Block upperDirt = dirt.getLocation().add(0, 1, 0).getBlock();
				if(dirt.getType().equals(Materials.SOIL) && dirt.getData() < 7) {
					Adapter.getAdapter().getLogger().warn("Not folly done: " + dirt.getData());
					continue;
				}
				boolean needNewPlant = upperDirt.getType() == Materials.AIR;
				Plantable plant = PlantableType.getPlantage(blockMaterial);
				if(plant == null) {
					for(ItemStack temp : p.getInventory().getContents()) {
						if(temp != null) {
							plant = plantableType.getPlantageHasInventoryItem(temp.getType());
							if(plant != null && (needNewPlant || plant.getNextItem() == upperDirt.getType()))
								break;
						}
					}
				}
				if (plant == null) {
					plant = PlantableType.getPlantage(upperDirt.getType());
					if(plant == null)
						continue;
				}
				if(plant.getNextItem() != upperDirt.getType()) {
					Plantable pl = PlantableType.getPlantage(upperDirt.getType());
					if(pl != null)
						plant = pl;
				}
				Material value = plant.getNextItem();
				if ((value == upperDirt.getType() && (plant.getNeededDataToGet() == -1 || plant.getNeededDataToGet() == upperDirt.getData())) || (needNewPlant && keepEmpty && upperDirt.getType() == Materials.AIR)) {
					tool.used(p, "BREAK", b);
					count++;
					boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
					LocationActions.add(upperDirt.getLocation(), p, !isRemoved);
					upperDirt.breakNaturally(inHand);
					upperDirt.setType(plant.getNextItem());
					//p.sendMessage("Block " + b.getType().name() + " : " + needNewPlant + ", " + keepEmpty + " > " + upperDirt.getType().name() +" / " + value.name() +" > " + plant.getNeededDataToGet() + " ! " + plant.getInventoryItem().name());
				}
			}
		}
		int counterDamage = (int) (count / tool.getConfigSection().getDouble("dura-reduction", 4));
		if(counterDamage == 0)
			counterDamage = 1;
		ItemUtils.damage(tool, p, inHand, slot, counterDamage);
		return count;
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
		for(int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack item = inv.get(slot);
			if(item != null && item.getType().equals(m)) {
				//p.sendMessage("Removing item " + item.getType().name());
				item.setAmount(item.getAmount() - 1);
				if(item.getAmount() == 0)
					inv.set(slot, null);
				else
					inv.set(slot, item);
				return true;
			}
		}
		return false;
	}
}

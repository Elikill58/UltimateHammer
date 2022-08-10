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
import com.elikill58.ultimatehammer.common.tools.hoe.HoeStateChecker;
import com.elikill58.ultimatehammer.common.tools.hoe.Plantable;
import com.elikill58.ultimatehammer.common.tools.hoe.Plantable.PlantableType;
import com.elikill58.ultimatehammer.common.tools.hoe.pickup.LocationActions;
import com.elikill58.ultimatehammer.universal.Scheduler;
import com.elikill58.ultimatehammer.universal.Version;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class HoeManager extends UltimateToolType implements Listeners {

	public static final String KEY = "hoe";

	public HoeManager() {
		super(KEY);
		add(PlantableType.BASIC, Materials.SEEDS, Materials.CROPS, (byte) 7);

		add(PlantableType.BASIC, Materials.POTATO_ITEM, Materials.POTATO, (byte) 7);
		add(PlantableType.BASIC, Materials.CARROT_ITEM, Materials.CARROT, (byte) 7);
			
		add(PlantableType.NETHER, Materials.NETHER_WART_ITEM, Materials.NETHER_WART, (byte) 3);
		if(Version.getVersion().isNewerOrEquals(Version.V1_9))
			add(PlantableType.BASIC, Materials.BEETROOT_SEEDS, Materials.BEETROOTS, (byte) 7);
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
		getToolForHand(p).forEach((tool) -> {
			if (WorldRegionBypass.cannotBuild(p, tool, baseBlock.getLocation()))
				return;
			int slot = p.getInventory().getHeldItemSlot();
			e.setCancelled(true);
			int nbChange = manageAllPlant(p, baseBlock, tool, e.getAction().name().contains("RIGHT"), false);
			if(nbChange <= 0)
				return;
			if(inHand != null) {
				if(inHand.getType().getMaxDurability() < inHand.getDurability() + nbChange)
					p.getInventory().set(slot, null);
				else
					inHand.addDamage((short) nbChange);
			}
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block baseBlock = e.getBlock();
		getToolForHand(p).forEach((tool) -> {
			Block b = e.getBlock();
			if(WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
				return;
			e.setCancelled(true);
			manageAllPlant(p, baseBlock, tool, true, true);
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onSpawn(ItemSpawnEvent e) {
		if (LocationActions.doAutoActions(e.getItem(), e.getLocation())) {
			e.setCancelled(true);
		}
	}
	
	public static int manageAllPlant(Player p, Block baseBlock, UltimateTool tool, boolean keepEmpty, boolean fromBreak) {
		Material m = baseBlock.getType();
		if (m == Materials.AIR) {
			baseBlock = baseBlock.getLocation().sub(0, 1, 0).getBlock();
			m = baseBlock.getType();
		}
		PlantableType plantableType = PlantableType.getPlantageType(m);
		if (plantableType == null) {
			plantableType = PlantableType
					.getPlantageType(baseBlock.getLocation().sub(0, 1, 0).getBlock().getType());
			if (plantableType == null)
				return 0;
		}
		ItemStack inHand = p.getItemInHand();
		int slot = p.getInventory().getHeldItemSlot();
		World w = baseBlock.getWorld();
		int count = 0;
		int amount = 1;
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
					if (up.getType().getId().contains("GRASS"))
						up.setType(Materials.AIR);
					continue;
				} else if (dirt.getType().equals(Materials.SOIL) && !HoeStateChecker.hasHumidity(dirt)) {
					continue;
				}
				if (!plantableType.getMaterial().contains(dirt.getType())) {
					continue;
				}
				Block upperDirt = dirt.getLocation().add(0, 1, 0).getBlock();
				boolean needNewPlant = upperDirt.getType() == Materials.AIR;
				Plantable plant = PlantableType.getPlantage(blockMaterial);
				if (plant == null) {
					for (ItemStack temp : p.getInventory().getContents()) {
						if (temp != null) {
							plant = plantableType.getPlantageHasInventoryItem(temp.getType());
							if (plant != null && (needNewPlant || plant.getNextItem() == upperDirt.getType()))
								break;
						}
					}
				}
				if (plant == null) {
					plant = PlantableType.getPlantage(upperDirt.getType());
					if (plant == null)
						continue;
				}
				if (plant.getNextItem() != upperDirt.getType()) {
					Plantable pl = PlantableType.getPlantage(upperDirt.getType());
					if (pl != null)
						plant = pl;
				}
				Material value = plant.getNextItem();
				if ((value == upperDirt.getType()
						&& (plant.getNeededDataToGet() == -1 || HoeStateChecker.hasReachAge(upperDirt, plant.getNeededDataToGet())))
						|| (needNewPlant && keepEmpty && upperDirt.getType() == Materials.AIR)) {
					if(!tool.usedBreak(p, b)) {
						count++;
						boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
						LocationActions.add(upperDirt.getLocation(), p, !isRemoved);
						upperDirt.breakNaturally(inHand);
						upperDirt.setType(plant.getNextItem());
					}
				}
			}
		}
		int counterDamage = (int) (count / tool.getConfigSection().getDouble("dura-reduction", 4));
		if (counterDamage == 0)
			counterDamage = 1;
		ItemUtils.damage(tool, p, inHand, slot, counterDamage);
		return count;
		
		/*Adapter log = Adapter.getAdapter();
		if(baseBlock.getType().equals(Materials.AIR)) {
			baseBlock = baseBlock.getLocation().sub(0, 1, 0).getBlock();
		}
		Material m = baseBlock.getType();
		PlantableType plantableType = PlantableType.getPlantageType(m);
		if (plantableType == null) {
			plantableType = PlantableType.getPlantageType(baseBlock.getLocation().clone().sub(0, 1, 0).getBlock().getType());
			if(plantableType == null) {
				log.debug("No plantable type founded for " + baseBlock.getLocation().clone().sub(0, 1, 0).getBlock().getType().getId());
				return 0;
			}
		}
		ItemStack inHand = p.getItemInHand();
		int slot = p.getInventory().getHeldItemSlot();
		World w = baseBlock.getWorld();
		int count = 0;
		int amount = (tool.getHoeSize() - 1) / 2;
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
				} else if(dirt.getType().equals(Materials.SOIL) && !HoeStateChecker.hasHumidity(dirt)) {
					log.debug("Wrong data for soil " + dirt.getData());
					continue;
				}
				if (!plantableType.getMaterial().contains(dirt.getType())) {
					log.debug("Type: " + plantableType + " not containing " + dirt.getType().getId());
					continue;
				}
				Block upperDirt = dirt.getLocation().add(0, 1, 0).getBlock();
				boolean needNewPlant = upperDirt.getType().equals(Materials.AIR);
				Plantable plant = PlantableType.getPlantage(blockMaterial);
				if(plant == null) {
					for(ItemStack temp : p.getInventory().getContents()) {
						if(temp != null) {
							plant = plantableType.getPlantageHasInventoryItem(temp.getType());
							if(plant != null)
								log.debug("Plant " + temp.getType().getId() + " > " + plant);
							if(plant != null && (needNewPlant || plant.getNextItem() == upperDirt.getType()))
								break;
						}
					}
				}
				if (plant == null) {
					plant = PlantableType.getPlantage(upperDirt.getType());
					if(plant == null) {
						log.debug("Failed to find plant: " + upperDirt.getType().getId() + " (old: " + blockMaterial.getId() + ")");
						continue;
					}
				}
				if(plant.getNextItem() != upperDirt.getType()) {
					Plantable pl = PlantableType.getPlantage(upperDirt.getType());
					if(pl != null)
						plant = pl;
				}
				Material value = plant.getNextItem();
				if ((value.equals(upperDirt.getType()) && (plant.getNeededDataToGet() == -1 || plant.getNeededDataToGet() == upperDirt.getData())) || (needNewPlant && !fromBreak && upperDirt.getType().equals(Materials.AIR))) {
					if(!tool.usedBreak(p, b)) {
						count++;
						boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
						LocationActions.add(upperDirt.getLocation(), p, !isRemoved);
						upperDirt.breakNaturally(inHand);
						upperDirt.setType(plant.getNextItem());
					}
				}
			}
		}
		int counterDamage = (int) (count / tool.getConfigSection().getDouble("dura-reduction", 4));
		if(counterDamage == 0)
			counterDamage = 1;
		ItemUtils.damage(tool, p, inHand, slot, counterDamage);
		return count;*/
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

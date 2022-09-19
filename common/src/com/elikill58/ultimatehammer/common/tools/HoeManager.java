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
import com.elikill58.ultimatehammer.api.location.Location;
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

		add(PlantableType.BASIC, Materials.POTATO_ITEM, Materials.POTATOES, (byte) 7);
		add(PlantableType.BASIC, Materials.CARROT_ITEM, Materials.CARROTS, (byte) 7);

		add(PlantableType.NETHER, Materials.NETHER_WART_ITEM, Materials.NETHER_WART, (byte) 3);
		add(PlantableType.WOOD, Materials.CACAO_ITEM, Materials.CACAO, (byte) 3);
		if (Version.getVersion().isNewerOrEquals(Version.V1_9))
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
			if(managePlants(p, b, tool, e.getAction().name().contains("RIGHT"), e.getFace()) > 0) {
				e.setCancelled(true); // don't cancel if nothing change
				Scheduler.getInstance().runDelayed(p::updateInventory, 2);
			}
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
			managePlants(p, b, tool, true, BlockFace.SELF);
			Scheduler.getInstance().runDelayed(p::updateInventory, 2);
		});
	}

	@EventListener
	public void onSpawn(ItemSpawnEvent e) {
		if (LocationActions.doAutoActions(e.getItem(), e.getLocation())) {
			e.setCancelled(true);
		}
	}

	public int managePlants(Player p, Block baseBlock, UltimateTool tool, boolean keepEmpty, BlockFace baseFacing) {
		PlantableType plantableType = PlantableType.getPlantageTypeForBaseBlock(baseBlock);
		if (plantableType == null) {
			Adapter.getAdapter().debug("Can't find valid plantable type for " + baseBlock.getType().getId() + " and facing " + baseBlock.getBlockData().getFacing());
			return 0;
		}
		ItemStack inHand = p.getItemInHand();
		int slot = p.getInventory().getHeldItemSlot();
		World w = baseBlock.getWorld();
		int count = 0, size = (tool.getHoeSize() - 1) / 2;
		Location loc = baseBlock.getLocation();
		int x1 = loc.getBlockX(), x2 = x1;
		int y1 = loc.getBlockY(), y2 = y1;
		int z1 = loc.getBlockZ(), z2 = z1;
		Adapter.getAdapter().debug("Bases: " + size + " > " + x1 + ", " + y1 + ", " + z2);
		if (Math.abs(p.getLocation().getPitch()) <= 30 && plantableType.isVertical()) {
			y1 -= size;
			y2 += size;
			float yaw = Math.abs(p.getLocation().getYaw());
			if ((yaw >= 45 && yaw <= 135) || (yaw <= 315 && yaw >= 225)) {
				// move selon Z
				z1 -= size;
				z2 += size;
			} else {
				// move selon X
				x1 -= size;
				x2 += size;
			}
		} else {
			x1 -= size;
			x2 += size;
			z1 -= size;
			z2 += size;
		}
		Adapter.getAdapter().debug("Dir: " + x1 + ">" + x2 + ", " + y1 + ">" + y2 + ", " + z1 + ">" + z2);
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					Block checking = w.getBlockAt(x, y, z); // get new block
					if (WorldRegionBypass.cannotBuild(p, tool, checking.getLocation())) // not allowed to build
						continue;
					BlockFace facing = checking.getBlockData().getFacing();
					Block dirt = checking.getRelative(facing == null ? (plantableType.isVertical() ? BlockFace.SELF : BlockFace.DOWN) : facing); // get dirt below
					if (!plantableType.getMaterial().contains(dirt.getType())) // near to invalid block (sand, gravel etc)
						continue;
					if (dirt.getType().equals(Materials.GRASS) || dirt.getType().equals(Materials.DIRT)) {
						// not soil but set it to
						if (!checking.getType().isTransparent()) // don't change type when upper it's not like grass
							continue;
						boolean notAllowed = Adapter.getAdapter().callBreakEvent(checking, p);
						if(notAllowed)
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
					} else if(checking.getType().equals(Materials.JUNGLE_LOG)/* && !checking.getType().equals(Materials.CACAO)*/) {
						Block next = checking.getRelative(baseFacing);
						if(next.getType().isTransparent()) {
							if(tryToRemoveFirstItem(p, Materials.CACAO_ITEM)) {
								next.setType(Materials.CACAO);
								Adapter.getAdapter().debug("Change facing " + baseFacing + " > " + baseFacing.getOppositeFace() + ", checking: " + checking + ", dirt: " + dirt);
								next.getBlockData().setFacing(baseFacing.getOppositeFace());
							} else
								Adapter.getAdapter().debug("Can't remove item " + next.getType().getId());
						}
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
					if(plant == null)
						Adapter.getAdapter().debug("Plant: null for " + checkingMaterial.getId() + ", facing: " + facing);
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
						boolean notAllowed = Adapter.getAdapter().callBreakEvent(checking, p);
						if(notAllowed)
							continue;
						if (!tool.usedBreak(p, checking)) {
							count++;
							boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
							LocationActions.add(checking.getLocation(), p, !isRemoved);
							facing = checking.getBlockData().getFacing();
							checking.breakNaturally(inHand);
							checking.setType(plant.getNextItem());
							if(plantableType.isVertical()) {
								checking.getBlockData().setFacing(facing == null ? baseFacing : facing);
								Adapter.getAdapter().debug("Set facing " + facing + ", done: " + dirt);
							}
						}
					} else
						Adapter.getAdapter().debug("Can't validate: " + data.getAge());
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

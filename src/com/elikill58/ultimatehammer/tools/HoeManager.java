package com.elikill58.ultimatehammer.tools;

import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.tools.hoe.LocationActions;
import com.elikill58.ultimatehammer.tools.hoe.Plantable;
import com.elikill58.ultimatehammer.tools.hoe.Plantable.PlantableType;
import com.elikill58.ultimatehammer.utils.HoeStateChecker;
import com.elikill58.ultimatehammer.utils.ItemUtils;
import com.elikill58.ultimatehammer.utils.Utils;
import com.elikill58.ultimatehammer.utils.Version;

public class HoeManager extends UltimateTool implements Listener {

	private int nbBlocked;

	public HoeManager(UltimateHammer pl) {
		super(pl, "hoe");
		nbBlocked = 0;
		Version v = Version.getVersion();
		add(PlantableType.BASIC, ItemUtils.SEEDS, ItemUtils.CROPS, (byte) 7);

		if(v.isNewerOrEquals(Version.V1_18)) {
			add(PlantableType.BASIC, Material.valueOf("POTATO"), Material.valueOf("POTATOES"), (byte) 7);
			add(PlantableType.BASIC, Material.valueOf("CARROT"), Material.valueOf("CARROTS"), (byte) 7);
		} else {
			add(PlantableType.BASIC, ItemUtils.POTATO_ITEM, Material.POTATO, (byte) 7);
			add(PlantableType.BASIC, ItemUtils.CARROT_ITEM, Material.CARROT, (byte) 7);
		}
		add(PlantableType.NETHER, ItemUtils.NETHER_STALK, ItemUtils.NETHER_WARTS, (byte) 3);
		if(v.isNewerOrEquals(Version.V1_9))
			add(PlantableType.BASIC, Material.BEETROOT_SEEDS, Material.BEETROOTS, (byte) 7);
		if (nbBlocked > 0)
			pl.getLogger().info("Loaded " + nbBlocked + " disabled items for hoe.");
	}
	
	private void add(PlantableType type, Material invItem, Material nextItem, byte neededDataToGet) {
		Stream<String> stream = getConfigSection().getStringList("disabled").stream();
		if (!stream.filter((msg) -> msg.equalsIgnoreCase(invItem.name()) || msg.equalsIgnoreCase(nextItem.name()))
				.findFirst().isPresent()) {
			type.addPlantage(new Plantable(invItem, nextItem, neededDataToGet));
		} else
			nbBlocked++;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null || e.getAction().equals(Action.PHYSICAL))
			return;
		Player p = e.getPlayer();
		Block baseBlock = e.getClickedBlock();
		ItemStack inHand = Utils.getItemInHand(p);
		if (!isItem(inHand))
			return;
		if (WorldRegionBypass.cannotBuild(p, this, baseBlock.getLocation()))
			return;
		int slot = p.getInventory().getHeldItemSlot();
		e.setCancelled(true);
		manageAllPlant(p, baseBlock.getLocation().add(0, 1, 0).getBlock(), this, e.getAction().name().contains("RIGHT"),
				false);
		if (inHand != null) {
			if (ItemUtils.getUseAmount(p, inHand) <= 0)
				p.getInventory().setItem(slot, null);
			else
				p.getInventory().setItem(slot, inHand);
		}
		Bukkit.getScheduler().runTaskLater(getPlugin(), p::updateInventory, 2);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSpawn(ItemSpawnEvent e) {
		if (LocationActions.doAutoActions(e.getEntity(), e.getLocation())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block baseBlock = e.getBlock();
		ItemStack inHand = Utils.getItemInHand(p);
		if (!isItem(inHand))
			return;
		Block b = e.getBlock();
		if (WorldRegionBypass.cannotBuild(p, this, b.getLocation()))
			return;
		e.setCancelled(true);
		manageAllPlant(p, baseBlock, this, true, true);
		Bukkit.getScheduler().runTaskLater(getPlugin(), p::updateInventory, 2);
	}

	public static int manageAllPlant(Player p, Block baseBlock, UltimateTool tool, boolean keepEmpty,
			boolean fromBreak) {
		Material m = baseBlock.getType();
		if (m == Material.AIR) {
			baseBlock = baseBlock.getLocation().subtract(0, 1, 0).getBlock();
			m = baseBlock.getType();
		}
		PlantableType plantableType = PlantableType.getPlantageType(m);
		if (plantableType == null) {
			plantableType = PlantableType
					.getPlantageType(baseBlock.getLocation().subtract(0, 1, 0).getBlock().getType());
			if (plantableType == null)
				return 0;
		}
		ItemStack inHand = Utils.getItemInHand(p);
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
				if (dirt.getType().equals(ItemUtils.GRASS) || dirt.getType().equals(Material.DIRT)) {
					dirt.setType(ItemUtils.SOIL);
					Block up = dirt.getLocation().add(0, 1, 0).getBlock();
					if (up.getType().name().contains("GRASS"))
						up.setType(Material.AIR);
					continue;
				} else if (dirt.getType().equals(ItemUtils.SOIL) && !HoeStateChecker.hasHumidity(dirt)) {
					continue;
				}
				if (!plantableType.getMaterial().contains(dirt.getType())) {
					continue;
				}
				Block upperDirt = dirt.getLocation().add(0, 1, 0).getBlock();
				boolean needNewPlant = upperDirt.getType() == Material.AIR;
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
						|| (needNewPlant && keepEmpty && upperDirt.getType() == Material.AIR)) {
					boolean isRemoved = tryToRemoveFirstItem(p, plant.getInventoryItem());
					LocationActions.add(upperDirt.getLocation(), p, !isRemoved);
					upperDirt.breakNaturally(inHand);
					upperDirt.setType(plant.getNextItem());
				}
			}
		}
		int counterDamage = (int) (count / tool.getConfigSection().getDouble("dura-reduction", 4));
		if (counterDamage == 0)
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
		for (int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack item = inv.getItem(slot);
			if (item != null && item.getType().equals(m)) {
				// p.sendMessage("Removing item " + item.getType().name());
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() == 0)
					inv.setItem(slot, null);
				else
					inv.setItem(slot, item);
				return true;
			}
		}
		return false;
	}
}

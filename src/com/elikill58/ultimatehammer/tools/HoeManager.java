package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import com.elikill58.ultimatehammer.tools.hoe.Plantable;
import com.elikill58.ultimatehammer.tools.hoe.Plantable.PlantableType;
import com.elikill58.ultimatehammer.tools.hoe.ToClean;
import com.elikill58.ultimatehammer.utils.ItemUtils;
import com.elikill58.ultimatehammer.utils.Utils;

public class HoeManager extends UltimateTool implements Listener {

	private final List<ToClean> mustCleanItem = new ArrayList<>();
	private int nbBlocked;

	public HoeManager(UltimateHammer pl) {
		super(pl, "hoe");
		nbBlocked = 0;
		add(PlantableType.BASIC, ItemUtils.SEEDS, ItemUtils.CROPS);
		add(PlantableType.BASIC, Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM);
		add(PlantableType.BASIC, Material.MELON_SEEDS, Material.MELON_STEM);
		add(PlantableType.BASIC, ItemUtils.POTATO_ITEM, Material.POTATO);
		add(PlantableType.BASIC, ItemUtils.CARROT_ITEM, Material.CARROT);
		add(PlantableType.NETHER, ItemUtils.NETHER_STALK, ItemUtils.NETHER_WARTS);
		if(nbBlocked > 0)
			pl.getLogger().info("Loaded " + nbBlocked + " disabled items for hoe.");
	}
	
	private void add(PlantableType type, Material invItem, Material nextItem) {
		Stream<String> stream = getConfigSection().getStringList("disabled").stream();
		if(!stream.filter((msg) -> msg.equalsIgnoreCase(invItem.name()) || msg.equalsIgnoreCase(nextItem.name())).findFirst().isPresent()) {
			type.addPlantage(new Plantable(invItem, nextItem));
		} else
			nbBlocked++;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!isEnabled())
			return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() == null)
			return;
		Material m = e.getClickedBlock().getType();
		PlantableType plantableType = PlantableType.getPlantageType(m);
		if (plantableType == null)
			return;
		Player p = e.getPlayer();
		if (!isItem(Utils.getItemInHand(p)))
			return;
		if(WorldRegionBypass.cannotBuild(p, this, e.getClickedBlock().getLocation()))
			return;
		final Editing editing = new Editing();
		PlayerInventory inv = p.getInventory();
		for (ItemStack it : inv.getContents()) {
			if (it == null || editing.isSet())
				continue;
			Plantable plant = plantableType.getPlantageHasInventoryItem(it.getType());
			if (plant != null) {
				editing.setItemAndPlant(it, plant);
				break;
			}
		}
		
		if (editing.isSet()) {
			final Block b = e.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
			if (!b.getType().name().contains("AIR"))
				return;
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					ItemStack it = editing.getItem();
					b.setType(editing.getPlant().getNextItem());
					if (it.getAmount() == 1)
						p.getInventory().remove(it);
					else
						it.setAmount(it.getAmount() - 1);
					p.updateInventory();
				}
			}, 2);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e) {
		if (!isEnabled())
			return;
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (!isItem(Utils.getItemInHand(p)))
			return;
		if(WorldRegionBypass.cannotBuild(p, this, b.getLocation()))
			return;
		Material blockMaterial = b.getType();
		Plantable plant = PlantableType.getPlantage(blockMaterial);
		if(plant == null)
			return;
		Material value = plant.getNextItem();
		if (value == blockMaterial) {
			mustCleanItem.add(new ToClean(p, b.getLocation(), plant.getInventoryItem()));
			Bukkit.getScheduler().runTaskLater(getPlugin(), () -> e.getBlock().setType(value), 2);
		}
		
		/*for (Entry<Material, Material> entries : correspondance.entrySet()) {
			Material value = entries.getValue();
			if (value == blockMaterial) {
				mustCleanItem.add(new ToClean(p, b.getLocation(), entries.getKey()));
				Bukkit.getScheduler().runTaskLater(getPlugin(), () -> e.getBlock().setType(value), 2);
			}
		}*/
	}

	@EventHandler
	public void onSpawn(ItemSpawnEvent e) {
		Location loc = e.getLocation();
		Optional<ToClean> optToClean = mustCleanItem.stream().filter((to) -> to.getLocation().distance(loc) < 1.3).findFirst();
		if (optToClean.isPresent()) {
			ToClean to = optToClean.get();
			mustCleanItem.remove(to);
			ItemStack item = e.getEntity().getItemStack();
			if(item.getType() == to.getType()) {
				if(item.getAmount() == 1)
					e.setCancelled(true);
				else
					item.setAmount(item.getAmount() - 1);
			}
			e.getEntity().setItemStack(item);
		}
	}
	
	class Editing {
		private ItemStack item = null;
		private Plantable plant = null;
		
		public Editing() {}

		public ItemStack getItem() {
			return item;
		}

		public Plantable getPlant() {
			return plant;
		}

		public void setItemAndPlant(ItemStack item, Plantable plant) {
			this.item = item;
			this.plant = plant;
		}
		
		public boolean isSet() {
			return plant != null && item != null;
		}
	}
}

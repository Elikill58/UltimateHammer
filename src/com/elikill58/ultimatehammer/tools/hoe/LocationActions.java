package com.elikill58.ultimatehammer.tools.hoe;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.utils.Utils;

public class LocationActions {
	
	public static final HashMap<Location, LocationActions> locations = new HashMap<>();
	private final Player p;
	private boolean haveToKeep;

	private LocationActions(Player p, boolean haveToKeep) {
		this.p = p;
		this.haveToKeep = haveToKeep;
	}

	public static void add(Location loc, Player p, boolean haveToKeep) {
		Location location = loc.getBlock().getLocation();

		LocationActions sl = new LocationActions(p, haveToKeep);
		locations.put(location, sl);

		Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateHammer.getInstance(), () -> {
			if (locations.containsKey(location) && locations.get(location).equals(sl)) {
				locations.remove(location);
			}
		}, 10L);
	}

	public static boolean doAutoActions(Item item, Location exactLoc) {
		Location loc = exactLoc.getBlock().getLocation();

		if (item == null) {
			return false;
		}

		LocationActions sl = locations.get(loc);

		if (sl == null || !sl.p.isValid()) {
			return false;
		}
		if(!sl.haveToKeep) {
			sl.haveToKeep = true;
			return false;
		}
		ItemStack inHand = Utils.getItemInHand(sl.p);
		if (inHand == null)
			return false;
		for(UltimateTool tool : UltimateHammer.getInstance().allTools.values()) {
			if(!tool.isItem(inHand))
				return false;
		}
		return AutoPickup.pickup(sl.p, item.getItemStack());
	}
}

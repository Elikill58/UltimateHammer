package com.elikill58.ultimatehammer.common.tools.hoe.pickup;

import java.util.HashMap;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Scheduler;

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

		Scheduler.getInstance().runDelayed(() -> {
			if (locations.containsKey(location) && locations.get(location).equals(sl)) {
				locations.remove(location);
			}
		}, 10);
	}

	public static boolean doAutoActions(ItemStack item, Location exactLoc) {
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
		ItemStack inHand = sl.p.getItemInHand();
		if (inHand == null)
			return false;
		for(UltimateTool tool : UltimateTool.getAlltools().values()) {
			if(!tool.isItem(inHand))
				return false;
		}
		return AutoPickup.pickup(sl.p, item);
	}
}

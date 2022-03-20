package com.elikill58.ultimatehammer.universal.bypass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;

public class WorldRegionBypass {

	private static boolean IS_ENABLED = false;
	public static final List<WorldRegionBypass> REGIONS_BYPASS = new ArrayList<>();
	public static final List<BuildChecker> BUILD_CHECKER = new ArrayList<>();

	public static void load() {
		Configuration section = Adapter.getAdapter().getConfig().getSection("region-bypass");
		if(section != null) {
			IS_ENABLED = section.getBoolean("enabled", false);
			for(String keys : section.getKeys()) {
				if(keys.equalsIgnoreCase("enabled"))
					continue;
				new WorldRegionBypass(section.getSection(keys));
			}
			if(IS_ENABLED)
				Adapter.getAdapter().getLogger().info("Region bypass enabled, in " + REGIONS_BYPASS.size() + " regions/worlds.");
		} else
			IS_ENABLED = false;
		
	}
	
	public static boolean cannotBuild(Player p, UltimateTool uh, Location loc) {
		for(BuildChecker bc : BUILD_CHECKER)
			if(!bc.canBuild(p, loc))
				return true;
		if(!IS_ENABLED || REGIONS_BYPASS.isEmpty())
			return false;
		List<WorldRegionBypass> list = REGIONS_BYPASS.stream().distinct().filter((by) -> by.canApply(uh.getKey())).collect(Collectors.toList());
		if(list.isEmpty())
			return false;
		for(WorldRegionBypass bypass : list) {
			if(bypass.getWorlds().contains(loc.getWorld().getName().toLowerCase()))
				return true;
			/*if(UltimateHammer.worldGuardSupport && WorldGuardSupport.isInAreas(loc, bypass.getRegions()))
				return true;*/
		}
		return false;
	}
	
	private final List<String> features;
	private final List<String> regions, worlds;
	
	public WorldRegionBypass(Configuration section) {
		features = section.getStringList("features");
		regions = section.getStringList("regions");
		worlds = section.getStringList("worlds").stream().map(String::toLowerCase).collect(Collectors.toList());
		
		REGIONS_BYPASS.add(this);
	}
	
	public List<String> getFeatures() {
		return features;
	}
	
	public boolean canApply(String s) {
		for(String temp : features)
			if(temp.equalsIgnoreCase(s))
				return true;
		return false;
	}

	public List<String> getRegions() {
		return regions;
	}

	public List<String> getWorlds() {
		return worlds;
	}
}

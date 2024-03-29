package com.elikill58.ultimatehammer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.support.WorldGuardSupport;
import com.elikill58.ultimatehammer.support.factions.BuildChecker;

public class WorldRegionBypass {

	private static boolean IS_ENABLED = false;
	public static final List<WorldRegionBypass> REGIONS_BYPASS = new ArrayList<>();
	public static final List<BuildChecker> BUILD_CHECKER = new ArrayList<>();

	public static void load(UltimateHammer pl) {
		ConfigurationSection section = pl.getConfig().getConfigurationSection("region-bypass");
		if(section != null) {
			IS_ENABLED = section.getBoolean("enabled", false);
			for(String keys : section.getKeys(false)) {
				if(keys.equalsIgnoreCase("enabled"))
					continue;
				new WorldRegionBypass(section.getConfigurationSection(keys));
			}
			if(IS_ENABLED)
				pl.getLogger().info("Region bypass enabled, in " + REGIONS_BYPASS.size() + " regions/worlds.");
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
			if(UltimateHammer.worldGuardSupport && WorldGuardSupport.isInAreas(loc, bypass.getRegions()))
				return true;
		}
		return false;
	}
	
	private final List<String> features;
	private final List<String> regions, worlds;
	
	public WorldRegionBypass(ConfigurationSection section) {
		features = section.getStringList("features");
		regions = section.getStringList("regions");
		worlds = section.getStringList("worlds").stream().map((s) -> s.toLowerCase()).collect(Collectors.toList());
		
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

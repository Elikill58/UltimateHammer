package com.elikill58.ultimatehammer.support.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface BuildChecker {

	public boolean canBuild(Player p, Location loc);
}

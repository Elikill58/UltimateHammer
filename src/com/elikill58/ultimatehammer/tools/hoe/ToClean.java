package com.elikill58.ultimatehammer.tools.hoe;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ToClean {
	private final Player player;
	private final Location loc;
	private final Material type;

	public ToClean(Player player, Location loc, Material type) {
		this.player = player;
		this.loc = loc;
		this.type = type;
	}

	public Player getPlayer() {
		return player;
	}

	public Location getLocation() {
		return loc;
	}

	public Material getType() {
		return type;
	}
}

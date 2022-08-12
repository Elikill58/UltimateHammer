package com.elikill58.ultimatehammer.spigot.nms;

import static com.elikill58.ultimatehammer.spigot.utils.Utils.VERSION;

import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.nms.VersionAdapter;

public abstract class SpigotVersionAdapter extends VersionAdapter<Player> {
	
	protected static final String NBT_TAG_KEY = "ultimatehammer-v2";

	public SpigotVersionAdapter(String version) {
		super(version);
	}
	
	private static SpigotGlobalVersion instance = new SpigotGlobalVersion(VERSION);

	public static SpigotGlobalVersion getVersionAdapter() {
		return instance;
	}
}

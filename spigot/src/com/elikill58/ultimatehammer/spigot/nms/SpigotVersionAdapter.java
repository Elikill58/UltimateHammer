package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.nms.VersionAdapter;
import com.elikill58.ultimatehammer.universal.Adapter;

public abstract class SpigotVersionAdapter extends VersionAdapter<Player> {

	public SpigotVersionAdapter(String version) {
		super(version);
	}
	
	private static SpigotVersionAdapter instance;

	public static SpigotVersionAdapter getVersionAdapter() {
		if(instance == null) {
			if(Adapter.getAdapter().getVersion() == "")
				instance = new Paper1_21Version();
			else
				instance = new SpigotGlobalVersion(Adapter.getAdapter().getVersion());;
		}
		return instance;
	}
}

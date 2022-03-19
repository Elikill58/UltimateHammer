package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.SpigotUltimateHammer;

public class Spigot_UnknowVersion extends SpigotVersionAdapter {
	
	public Spigot_UnknowVersion(String version) {
		super(version);
		SpigotUltimateHammer.getInstance().getLogger().warning("Failed to find version adapter for " + version + ".");
	}
	
	@Override
	public int getXpToDrop(Block b, int bonusLevel, ItemStack item) {
		return 0;
	}
	
	@Override
	public void sendPacket(Player p, Object packet) {}
}

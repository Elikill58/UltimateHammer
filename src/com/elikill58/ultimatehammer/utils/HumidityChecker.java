package com.elikill58.ultimatehammer.utils;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Farmland;

public class HumidityChecker {
	
	@SuppressWarnings("deprecation")
	public static boolean hasHumidity(Block b) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			BlockData bd = b.getBlockData();
			return bd != null && bd instanceof Farmland && ((Farmland) bd).getMoisture() == ((Farmland) bd).getMaximumMoisture();
		} else
			return b.getData() >= 7;
	}
}

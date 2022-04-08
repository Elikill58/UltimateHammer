package com.elikill58.ultimatehammer.utils;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Farmland;

@SuppressWarnings("deprecation")
public class HoeStateChecker {
	
	public static boolean hasHumidity(Block b) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			BlockData bd = b.getBlockData();
			return bd != null && bd instanceof Farmland && ((Farmland) bd).getMoisture() == ((Farmland) bd).getMaximumMoisture();
		} else
			return b.getData() >= 7;
	}
	
	public static boolean hasReachAge(Block b, byte max) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			BlockData bd = b.getBlockData();
			return bd != null && bd instanceof Ageable && ((Ageable) bd).getAge() == ((Ageable) bd).getMaximumAge();
		} else
			return b.getData() >= max;
	}
}

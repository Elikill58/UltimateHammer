package com.elikill58.ultimatehammer.common.tools.hoe;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockData;

public class HoeStateChecker {

	public static boolean hasHumidity(Block b) {
		BlockData bd = b.getBlockData();
		return bd.getHumidity() == bd.getMaximumHumidity();
	}
	
	public static boolean hasReachAge(Block b, int max) {
		BlockData bd = b.getBlockData();
		return bd.getAge() == (bd.getMaximumAge() == -1 ? max : bd.getMaximumAge());
	}
}

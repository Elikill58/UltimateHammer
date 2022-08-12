package com.elikill58.ultimatehammer.universal.support;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;

public interface UsedActionManager {

	/**
	 * Check when breaking block to check if cancel
	 * 
	 * @param tool the tool used for cancel
	 * @param p the player which break
	 * @param b the breaked block (before breaking)
	 * @return true if should cancel
	 */
	default boolean usedBreak(UltimateTool tool, Player p, ItemStack item, Block b) {
		return false;
	}
}

package com.elikill58.ultimatehammer.universal.support;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.common.UltimateTool;

public interface UsedActionManager {

	default void usedBreak(UltimateTool tool, Player p, Block b) {
		
	}
	
}

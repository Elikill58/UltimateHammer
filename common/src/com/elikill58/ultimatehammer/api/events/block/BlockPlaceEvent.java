package com.elikill58.ultimatehammer.api.events.block;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class BlockPlaceEvent extends PlayerEvent implements CancellableEvent {

	private final Block b;
	private boolean cancel;
	
	public BlockPlaceEvent(Player p, Block b) {
		super(p);
		this.b = b;
	}
	
	public Block getBlock() {
		return b;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}

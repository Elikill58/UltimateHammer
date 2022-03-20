package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerInteractEvent extends PlayerEvent implements CancellableEvent {
	
	private final Action action;
	private final Block block;
	private boolean cancel = false;
	
	public PlayerInteractEvent(Player p, Action action, Block block) {
		super(p);
		this.action = action;
		this.block = block;
	}
	
	public Action getAction() {
		return action;
	}
	
	public Block getBlock() {
		return block;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancel = b;
	}
	
	public enum Action {
		RIGHT_CLICK_AIR,
		RIGHT_CLICK_BLOCK,
		LEFT_CLICK_AIR,
		LEFT_CLICK_BLOCK,
		PHYSICAL
	}
}

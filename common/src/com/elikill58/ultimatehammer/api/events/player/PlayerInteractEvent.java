package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockFace;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerInteractEvent extends PlayerEvent implements CancellableEvent {
	
	private final Action action;
	private final Block block;
	private final BlockFace face;
	private boolean cancel = false;
	
	public PlayerInteractEvent(Player p, Action action, Block block, BlockFace face) {
		super(p);
		this.action = action;
		this.block = block;
		this.face = face;
	}
	
	public Action getAction() {
		return action;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public BlockFace getFace() {
		return face;
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

package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerRegainHealthEvent extends PlayerEvent implements CancellableEvent {

	private boolean cancel = false;
	
	public PlayerRegainHealthEvent(Player p) {
		super(p);
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

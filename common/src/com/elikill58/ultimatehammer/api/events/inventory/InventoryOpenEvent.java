package com.elikill58.ultimatehammer.api.events.inventory;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class InventoryOpenEvent extends PlayerEvent implements CancellableEvent {

	private boolean cancel = false;
	
	public InventoryOpenEvent(Player p) {
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

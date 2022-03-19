package com.elikill58.ultimatehammer.api.events.inventory;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;
import com.elikill58.ultimatehammer.api.inventory.Inventory;

public class InventoryCloseEvent extends PlayerEvent implements CancellableEvent {

	private final Inventory inv;
	private boolean cancel = false;
	
	public InventoryCloseEvent(Player p, Inventory inv) {
		super(p);
		this.inv = inv;
	}
	
	public Inventory getInventory() {
		return inv;
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

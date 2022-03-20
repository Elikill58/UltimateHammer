package com.elikill58.ultimatehammer.api.events.entity;

import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.Event;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.location.Location;

public class ItemSpawnEvent implements Event, CancellableEvent {
	
	private final ItemStack item;
	private final Location loc;
	private boolean cancel = false;
	
	public ItemSpawnEvent(ItemStack item, Location loc) {
		this.loc = loc;
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public Location getLocation() {
		return loc;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean b) {
		cancel = b;
	}
}

package com.elikill58.ultimatehammer.api.events;

public interface CancellableEvent extends Event {

	public boolean isCancelled();
	
	public void setCancelled(boolean b);
}

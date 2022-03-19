package com.elikill58.ultimatehammer.api.events;

import com.elikill58.ultimatehammer.api.entity.Player;

public abstract class PlayerEvent implements Event {

	private final Player p;
	
	public PlayerEvent(Player p) {
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
}

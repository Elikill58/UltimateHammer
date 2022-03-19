package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;
import com.elikill58.ultimatehammer.api.location.Location;

public class PlayerTeleportEvent extends PlayerEvent {

	private final Location from, to;
	
	public PlayerTeleportEvent(Player p, Location from, Location to) {
		super(p);
		this.from = from;
		this.to = to;
	}
	
	public Location getFrom() {
		return from;
	}
	
	public Location getTo() {
		return to;
	}
}

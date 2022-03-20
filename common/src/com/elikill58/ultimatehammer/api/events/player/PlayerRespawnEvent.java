package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;
import com.elikill58.ultimatehammer.api.location.Location;

public class PlayerRespawnEvent extends PlayerEvent {

	private final Location respawnLocation;
	
	public PlayerRespawnEvent(Player p, Location respawnLocation) {
		super(p);
		this.respawnLocation = respawnLocation;
	}
	
	public Location getRespawnLocation() {
		return respawnLocation;
	}
}

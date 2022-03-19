package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerDeathEvent extends PlayerEvent {

	public PlayerDeathEvent(Player p) {
		super(p);
	}
	
}

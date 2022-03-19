package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerConnectEvent extends PlayerEvent {

	private final UltimateHammerPlayer np;
	private String joinMessage;
	
	public PlayerConnectEvent(Player p, UltimateHammerPlayer np, String joinMessage) {
		super(p);
		this.np = np;
		this.joinMessage = joinMessage;
	}
	
	public UltimateHammerPlayer getNegativityPlayer() {
		return np;
	}
	
	public String getJoinMessage() {
		return joinMessage;
	}
	
	public void setJoinMessage(String joinMessage) {
		this.joinMessage = joinMessage;
	}
}

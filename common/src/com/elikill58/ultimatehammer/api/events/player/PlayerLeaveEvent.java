package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerLeaveEvent extends PlayerEvent {

	private final UltimateHammerPlayer np;
	private String quitMessage;
	
	public PlayerLeaveEvent(Player p, UltimateHammerPlayer np, String quitMessage) {
		super(p);
		this.np = np;
		this.quitMessage = quitMessage;
	}
	
	public UltimateHammerPlayer getNegativityPlayer() {
		return np;
	}
	
	public String getQuitMessage() {
		return quitMessage;
	}
	
	public void setQuitMessage(String quitMessage) {
		this.quitMessage = quitMessage;
	}
}

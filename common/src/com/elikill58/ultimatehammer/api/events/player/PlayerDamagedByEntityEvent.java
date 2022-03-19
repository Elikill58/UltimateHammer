package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Entity;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;

public class PlayerDamagedByEntityEvent extends PlayerEvent implements CancellableEvent {

	private boolean cancel;
	private final Entity damager;
	
	public PlayerDamagedByEntityEvent(Player cible, Entity damager) {
		super(cible);
		this.damager = damager;
	}

	public Entity getDamager() {
		return damager;
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

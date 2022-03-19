package com.elikill58.ultimatehammer.api.events.player;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;
import com.elikill58.ultimatehammer.api.location.Location;

public class PlayerMoveEvent extends PlayerEvent implements CancellableEvent {

	private Location from, to;
	private final boolean movePos, moveLook;
	private boolean cancel = false, hasToSet = false;
	
	public PlayerMoveEvent(Player p, Location from, Location to) {
		super(p);
		this.from = from;
		this.to = to;
		this.moveLook = from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch();
		this.movePos = from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
	}
	
	public Location getTo() {
		return to.clone();
	}
	
	public void setTo(Location to) {
		this.to = to;
		this.hasToSet = true;
	}
	
	public Location getFrom() {
		return from.clone();
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public boolean hasToSet() {
		return hasToSet;
	}
	
	/**
	 * Know if the current move event concern a movement of look (yaw/pitch)
	 * 
	 * @return true if player is movement where he looks
	 */
	public boolean isMoveLook() {
		return moveLook;
	}
	
	/**
	 * Know if the current move event concern a physical movement (x/y/z)
	 * 
	 * @return true if really moving
	 */
	public boolean isMovePosition() {
		return movePos;
	}
}

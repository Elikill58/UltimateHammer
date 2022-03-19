package com.elikill58.ultimatehammer.api.entity;

import com.elikill58.ultimatehammer.api.location.Vector;

public abstract class AbstractEntity implements Entity {
	
	private Vector velocity = null;
	
	@Override
	public void applyTheoricVelocity() {
		this.velocity = getTheoricVelocity();
	}
	
	@Override
	public Vector getVelocity() {
		return velocity == null ? getTheoricVelocity() : velocity;
	}
	
	@Override
	public String toString() {
		return "Entity{id=" + getEntityId() + ",type=" + getType().name() + ",location=" + getLocation() + "}";
	}
}

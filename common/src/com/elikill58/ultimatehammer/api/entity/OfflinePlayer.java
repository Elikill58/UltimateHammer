package com.elikill58.ultimatehammer.api.entity;

import java.util.UUID;

import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.Vector;

public interface OfflinePlayer extends Entity {

	UUID getUniqueId();
	
	boolean isOnline();
	
	@Override
	default boolean isOnGround() {
		return true;
	}
	
	@Override
	default Location getLocation() {
		return null;
	}
	
	@Override
	default EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	default Location getEyeLocation() {
		return null;
	}

	@Override
	default Vector getRotation() {
		return null;
	}
	
	@Override
	default Vector getVelocity() {
		return null;
	}
	
	@Override
	default Vector getTheoricVelocity() {
		return null;
	}
	
	@Override
	default void setVelocity(Vector vel) {
		
	}

	@Override
	default void sendMessage(String msg) {}
	
	@Override
	default double getEyeHeight() {
		return 0;
	}
}
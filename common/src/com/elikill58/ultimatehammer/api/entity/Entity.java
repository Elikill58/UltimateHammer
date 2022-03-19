package com.elikill58.ultimatehammer.api.entity;

import com.elikill58.ultimatehammer.api.commands.CommandSender;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.Vector;

public interface Entity extends CommandSender {

	/**
	 * Check if the given entity is currently on ground
	 * 
	 * @return true if is on ground
	 */
	boolean isOnGround();

	/**
	 * Check if the actual entity is OP or not
	 * 
	 * @return true if entity OP
	 */
	boolean isOp();
	
	/**
	 * Get the entity location
	 * 
	 * @return entity location
	 */
	Location getLocation();
	
	double getEyeHeight();
	
	Location getEyeLocation();
	
	/**
	 * Get the rotation (also called "direction") of the entity
	 * 
	 * @return vector of entity's direction
	 */
	Vector getRotation();
	
	/**
	 * Get the type of the entity
	 * 
	 * @return the entity type
	 */
	EntityType getType();
	
	/**
	 * Get the entity ID
	 * 
	 * @return the ID of the entity
	 */
	String getEntityId();
	
	/**
	 * Check if this entity can be applied to this id
	 * 
	 * @param id the id to check
	 * @return true if the same id
	 */
	default boolean isSameId(String id) {
		return id.equalsIgnoreCase(getEntityId());
	}
	
	/**
	 * Check if this entity can be applied to this other entity
	 * 
	 * @param other the entity
	 * @return true if the same id
	 */
	default boolean isSameId(Entity other) {
		return getEntityId().equalsIgnoreCase(other.getEntityId());
	}
	
	/**
	 * Get the bounding box of the current entity
	 * 
	 * @return the bounding box
	 */
	BoundingBox getBoundingBox();
	
	/**
	 * Get current entity velocity
	 * 
	 * @return the entity velocity
	 */
	Vector getVelocity();
	
	void applyTheoricVelocity();
	
	/**
	 * Get the velocity that the entity SHOULD be take
	 *
	 * @return the theoric (and platform) velocity
	 */
	Vector getTheoricVelocity();
	
	/**
	 * Edit the entity velocity
	 * 
	 * @param vel the new velocity
	 */
	void setVelocity(Vector vel);
}

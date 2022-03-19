package com.elikill58.ultimatehammer.api.events.entity;

import com.elikill58.ultimatehammer.api.entity.Entity;
import com.elikill58.ultimatehammer.api.events.Event;

public class ProjectileHitEvent implements Event {
	
	private final Entity entity;
	
	public ProjectileHitEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}

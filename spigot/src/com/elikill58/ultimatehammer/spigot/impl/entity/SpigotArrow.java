package com.elikill58.ultimatehammer.spigot.impl.entity;

import com.elikill58.ultimatehammer.api.entity.Arrow;
import com.elikill58.ultimatehammer.api.entity.Entity;
import com.elikill58.ultimatehammer.api.entity.EntityType;

public class SpigotArrow extends SpigotEntity<org.bukkit.entity.Arrow> implements Arrow {

	public SpigotArrow(org.bukkit.entity.Arrow arrow) {
		super(arrow);
	}
	
	@Override
	public Entity getShooter() {
		return SpigotEntityManager.getProjectile(entity.getShooter());
	}

	@Override
	public EntityType getType() {
		return EntityType.ARROW;
	}
}
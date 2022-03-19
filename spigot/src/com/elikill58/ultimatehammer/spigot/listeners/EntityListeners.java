package com.elikill58.ultimatehammer.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotEntityManager;

public class EntityListeners implements Listener {

	@EventHandler
	public void onEntityShoot(EntityShootBowEvent e) {
		EventManager.callEvent(new com.elikill58.ultimatehammer.api.events.entity.EntityShootBowEvent(SpigotEntityManager.getEntity(e.getEntity())));
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		EventManager.callEvent(new com.elikill58.ultimatehammer.api.events.entity.ProjectileHitEvent(SpigotEntityManager.getEntity(e.getEntity())));
	}
}

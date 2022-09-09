package com.elikill58.ultimatehammer.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotEntityManager;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;
import com.elikill58.ultimatehammer.spigot.impl.location.SpigotLocation;

public class EntityListeners implements Listener {

	@EventHandler
	public void onEntityShoot(EntityShootBowEvent e) {
		EventManager.callEvent(new com.elikill58.ultimatehammer.api.events.entity.EntityShootBowEvent(SpigotEntityManager.getEntity(e.getEntity())));
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		EventManager.callEvent(new com.elikill58.ultimatehammer.api.events.entity.ProjectileHitEvent(SpigotEntityManager.getEntity(e.getEntity())));
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e) {
		com.elikill58.ultimatehammer.api.events.entity.ItemSpawnEvent c = new com.elikill58.ultimatehammer.api.events.entity.ItemSpawnEvent(new SpigotItemStack(e.getEntity().getItemStack()), SpigotLocation.toCommon(e.getLocation()), e.isCancelled());
		EventManager.callEvent(c);
		e.setCancelled(c.isCancelled());
	}
}

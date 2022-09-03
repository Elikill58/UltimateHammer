package com.elikill58.ultimatehammer.spigot.listeners.version;

import java.util.stream.Collectors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.api.events.inventory.PrepareAnvilEvent;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotEntityManager;
import com.elikill58.ultimatehammer.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

public class Listener9Upper implements Listener {
	
	@EventHandler
	public void onAnvil(org.bukkit.event.inventory.PrepareAnvilEvent e) {
		if(e.getResult() == null)
			return;
		PrepareAnvilEvent event = new PrepareAnvilEvent(new SpigotInventory(e.getInventory()), new SpigotItemStack(e.getResult()), e.getViewers().stream().map(SpigotEntityManager::getEntity).collect(Collectors.toList()));
		EventManager.callEvent(event);
		e.setResult(event.getResult() == null ? null : (org.bukkit.inventory.ItemStack) event.getResult().getDefault());
		if(event.isClose()) {
			e.getView().close();
		}
	}

}

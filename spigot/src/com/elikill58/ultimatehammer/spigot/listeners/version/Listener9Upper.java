package com.elikill58.ultimatehammer.spigot.listeners.version;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.api.events.others.PrepareAnvilEvent;
import com.elikill58.ultimatehammer.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

public class Listener9Upper implements Listener {
	
	@EventHandler
	public void onAnvil(org.bukkit.event.inventory.PrepareAnvilEvent e) {
		PrepareAnvilEvent event = new PrepareAnvilEvent(new SpigotInventory(e.getInventory()), new SpigotItemStack(e.getResult()));
		EventManager.callEvent(event);
		e.setResult((org.bukkit.inventory.ItemStack) event.getResult().getDefault());
	}

}

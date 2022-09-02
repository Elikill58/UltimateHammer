package com.elikill58.ultimatehammer.spigot.listeners.version;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.api.events.others.PrepareAnvilEvent;
import com.elikill58.ultimatehammer.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

public class Listener8Lower implements Listener {
	
	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if(e.getClickedInventory() == null || !(e.getWhoClicked() instanceof Player) || e.getInventory().getType() == InventoryType.ANVIL)
			return;
		Inventory inv = e.getInventory();
		PrepareAnvilEvent event = new PrepareAnvilEvent(new SpigotInventory(e.getInventory()), new SpigotItemStack(inv.getItem(3)));
		EventManager.callEvent(event);
		inv.setItem(3, (org.bukkit.inventory.ItemStack) event.getResult().getDefault());
	}

}

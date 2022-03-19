package com.elikill58.ultimatehammer.spigot.impl.inventory;

import com.elikill58.ultimatehammer.api.inventory.InventoryHolder;
import com.elikill58.ultimatehammer.api.inventory.PlatformHolder;

public class SpigotInventoryHolder extends InventoryHolder {

	private final org.bukkit.inventory.InventoryHolder holder;
	
	public SpigotInventoryHolder(org.bukkit.inventory.InventoryHolder holder) {
		this.holder = holder;
	}

	public org.bukkit.inventory.InventoryHolder getHolder() {
		return holder;
	}

	@Override
	public PlatformHolder getBasicHolder() {
		return null;
	}

}

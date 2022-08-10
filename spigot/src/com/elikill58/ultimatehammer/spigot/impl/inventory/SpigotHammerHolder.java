package com.elikill58.ultimatehammer.spigot.impl.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.elikill58.ultimatehammer.api.inventory.PlatformHolder;

public class SpigotHammerHolder extends PlatformHolder implements InventoryHolder {

	private final PlatformHolder holder;
	
	public SpigotHammerHolder(PlatformHolder holder) {
		this.holder = holder;
	}

	@Override
	public PlatformHolder getBasicHolder() {
		return holder;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

}

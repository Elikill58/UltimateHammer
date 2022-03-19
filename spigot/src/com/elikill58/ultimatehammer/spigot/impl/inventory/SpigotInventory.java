package com.elikill58.ultimatehammer.spigot.impl.inventory;

import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.inventory.InventoryType;
import com.elikill58.ultimatehammer.api.inventory.UltimateHammerHolder;
import com.elikill58.ultimatehammer.api.inventory.PlatformHolder;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

public class SpigotInventory extends Inventory {

	private final String inventoryName;
	private final int size;
	private final SpigotNegativityHolder holder;
	private final org.bukkit.inventory.Inventory inv;
	
	public SpigotInventory(org.bukkit.inventory.Inventory inv) {
		this.inventoryName = "";
		this.size = inv.getSize();
		if(inv.getHolder() instanceof SpigotNegativityHolder) {
			this.holder = (SpigotNegativityHolder) inv.getHolder();
		} else
			this.holder = (inv.getHolder() == null ? null : new SpigotNegativityHolder(new SpigotInventoryHolder(inv.getHolder())));
		this.inv = inv;
	}
	
	public SpigotInventory(String inventoryName, int size, UltimateHammerHolder holder) {
		this.inventoryName = inventoryName;
		this.size = size;
		this.holder = new SpigotNegativityHolder(holder);
		this.inv = Bukkit.createInventory(this.holder, size, inventoryName);
	}
	
	@Override
	public ItemStack get(int slot) {
		org.bukkit.inventory.ItemStack item = inv.getItem(slot);
		if(item == null)
			return null;
		return new SpigotItemStack(item);
	}

	@Override
	public void set(int slot, ItemStack item) {
		inv.setItem(slot, (org.bukkit.inventory.ItemStack) item.getDefault());
	}

	@Override
	public void remove(int slot) {
		inv.setItem(slot, null);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public String getInventoryName() {
		return inventoryName;
	}

	@Override
	public @Nullable PlatformHolder getHolder() {
		return holder == null ? null : holder.getBasicHolder();
	}

	@Override
	public Object getDefault() {
		return inv;
	}

	@Override
	public InventoryType getType() {
		return InventoryType.valueOf(inv.getType().name());
	}

	@Override
	public void clear() {
		inv.clear();
	}

	@Override
	public void addItem(ItemStack build) {
		inv.addItem((org.bukkit.inventory.ItemStack) build.getDefault());
	}
	
	@Override
	public boolean contains(Material type) {
		return inv.contains((org.bukkit.Material) type.getDefault());
	}
}

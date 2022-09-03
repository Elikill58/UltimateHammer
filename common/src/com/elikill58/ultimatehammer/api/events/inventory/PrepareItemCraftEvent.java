package com.elikill58.ultimatehammer.api.events.inventory;

import com.elikill58.ultimatehammer.api.events.Event;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class PrepareItemCraftEvent implements Event {

	private final Inventory inv;
	private final ItemStack[] matrix;
	private ItemStack result;
	
	public PrepareItemCraftEvent(Inventory inv, ItemStack[] matrix, ItemStack result) {
		this.inv = inv;
		this.matrix = matrix;
		this.result = result;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public ItemStack[] getMatrix() {
		return matrix;
	}
	
	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}
}

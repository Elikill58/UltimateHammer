package com.elikill58.ultimatehammer.api.events.others;

import com.elikill58.ultimatehammer.api.events.Event;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class PrepareAnvilEvent implements Event {

	private final Inventory inventory;
	private ItemStack result;
	
	public PrepareAnvilEvent(Inventory inv, ItemStack result) {
		this.inventory = inv;
		this.result = result;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
	}
}

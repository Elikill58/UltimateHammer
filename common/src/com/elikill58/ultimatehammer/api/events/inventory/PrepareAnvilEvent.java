package com.elikill58.ultimatehammer.api.events.inventory;

import java.util.List;

import com.elikill58.ultimatehammer.api.entity.Entity;
import com.elikill58.ultimatehammer.api.events.Event;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class PrepareAnvilEvent implements Event {

	private final Inventory inventory;
	private boolean close = false;
	private ItemStack result;
	private final List<Entity> viewers;
	
	public PrepareAnvilEvent(Inventory inv, ItemStack result, List<Entity> viewers) {
		this.inventory = inv;
		this.result = result;
		this.viewers = viewers;
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
	
	public List<Entity> getViewers() {
		return viewers;
	}
	
	public boolean isClose() {
		return close;
	}
	
	public void setClose(boolean close) {
		this.close = close;
	}
}

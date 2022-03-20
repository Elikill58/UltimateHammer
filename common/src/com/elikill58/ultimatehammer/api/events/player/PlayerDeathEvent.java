package com.elikill58.ultimatehammer.api.events.player;

import java.util.List;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.PlayerEvent;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class PlayerDeathEvent extends PlayerEvent {

	private final List<ItemStack> drops;
	
	public PlayerDeathEvent(Player p, List<ItemStack> drops) {
		super(p);
		this.drops = drops;
	}
	
	public List<ItemStack> getDrops() {
		return drops;
	}
}

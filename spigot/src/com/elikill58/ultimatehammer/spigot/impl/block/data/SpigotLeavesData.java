package com.elikill58.ultimatehammer.spigot.impl.block.data;

import org.bukkit.block.data.type.Leaves;

import com.elikill58.ultimatehammer.api.block.data.LeavesData;

public class SpigotLeavesData extends LeavesData {

	private final Leaves l;
	
	public SpigotLeavesData(Leaves l) {
		this.l = l;
	}

	@Override
	public boolean isPersistent() {
		return l.isPersistent();
	}

	@Override
	public int getDistance() {
		return l.getDistance();
	}
}

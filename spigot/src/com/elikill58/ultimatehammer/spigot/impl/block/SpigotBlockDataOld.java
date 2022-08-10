package com.elikill58.ultimatehammer.spigot.impl.block;

import org.bukkit.block.Block;

import com.elikill58.ultimatehammer.api.block.BlockData;

@SuppressWarnings("deprecation")
public class SpigotBlockDataOld extends BlockData {

	private final Block b;
	
	public SpigotBlockDataOld(Block b) {
		this.b = b;
	}

	@Override
	public boolean isLeavesPersistent() {
		return false;
	}

	@Override
	public int getLeavesDistance() {
		return b.getData();
	}

	@Override
	public int getHumidity() {
		return b.getData();
	}

	@Override
	public int getMaximumHumidity() {
		return 7;
	}

	@Override
	public int getAge() {
		return b.getData();
	}

	@Override
	public int getMaximumAge() {
		return -1;
	}
}

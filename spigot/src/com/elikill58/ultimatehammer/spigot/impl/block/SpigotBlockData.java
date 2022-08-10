package com.elikill58.ultimatehammer.spigot.impl.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Leaves;

import com.elikill58.ultimatehammer.api.block.BlockData;

public class SpigotBlockData extends BlockData {

	private final Block b;
	
	public SpigotBlockData(Block b) {
		this.b = b;
	}

	@Override
	public boolean isLeavesPersistent() {
		return b.getState() instanceof Leaves && ((Leaves) b.getState()).isPersistent();
	}

	@Override
	public int getLeavesDistance() {
		return b.getState() instanceof Leaves ? ((Leaves) b.getState()).getDistance() : -1;
	}

	@Override
	public int getHumidity() {
		return b.getState() instanceof Farmland ? ((Farmland) b.getState()).getMoisture() : -1;
	}

	@Override
	public int getMaximumHumidity() {
		return b.getState() instanceof Farmland ? ((Farmland) b.getState()).getMaximumMoisture() : -1;
	}

	@Override
	public int getAge() {
		return b.getState() instanceof Ageable ? ((Ageable) b.getState()).getAge() : -1;
	}

	@Override
	public int getMaximumAge() {
		return b.getState() instanceof Ageable ? ((Ageable) b.getState()).getMaximumAge() : -1;
	}
}

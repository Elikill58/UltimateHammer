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
		return b.getBlockData() instanceof Leaves && ((Leaves) b.getBlockData()).isPersistent();
	}

	@Override
	public int getLeavesDistance() {
		return b.getBlockData() instanceof Leaves ? ((Leaves) b.getBlockData()).getDistance() : -1;
	}

	@Override
	public int getHumidity() {
		return b.getBlockData() instanceof Farmland ? ((Farmland) b.getBlockData()).getMoisture() : -1;
	}

	@Override
	public int getMaximumHumidity() {
		return b.getBlockData() instanceof Farmland ? ((Farmland) b.getBlockData()).getMaximumMoisture() : -1;
	}

	@Override
	public int getAge() {
		return b.getBlockData() instanceof Ageable ? ((Ageable) b.getBlockData()).getAge() : -1;
	}

	@Override
	public int getMaximumAge() {
		return b.getBlockData() instanceof Ageable ? ((Ageable) b.getBlockData()).getMaximumAge() : -1;
	}
}

package com.elikill58.ultimatehammer.spigot.impl.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.block.data.type.Leaves;

import com.elikill58.ultimatehammer.api.block.BlockData;
import com.elikill58.ultimatehammer.api.block.BlockFace;

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

	@Override
	public BlockFace getFacing() {
		return b.getBlockData() instanceof Directional ? BlockFace.valueOf(((Directional) b.getBlockData()).getFacing().name()) : null;
	}
	
	@Override
	public void setFacing(BlockFace face) {
		org.bukkit.block.data.BlockData bd = b.getBlockData();
		if(bd instanceof Directional)
			((Directional) bd).setFacing(org.bukkit.block.BlockFace.valueOf(face.name()));
		b.setBlockData(bd);
	}
}

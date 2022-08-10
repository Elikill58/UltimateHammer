package com.elikill58.ultimatehammer.api.block;

import java.util.List;
import java.util.Objects;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;

public abstract class Block implements UltimateHammerObject {

	public abstract Location getLocation();
	
	public abstract Material getType();

	public abstract int getX();
	public abstract int getY();
	public abstract int getZ();

	public Block getRelative(BlockFace blockFace) {
		return getRelative(blockFace.getModX(), blockFace.getModY(), blockFace.getModY());
	}
	public abstract Block getRelative(int x, int y, int z);

	public abstract boolean isLiquid();

	public abstract void setType(Material type);
	
	public abstract boolean isWaterLogged();
	
	public abstract World getWorld();
	
	public void breakNaturally() {
		breakNaturally(null);
	}

	public abstract void breakNaturally(ItemStack item);

	public List<ItemStack> getDrops() {
		return getDrops(null);
	}

	public abstract List<ItemStack> getDrops(ItemStack item);
	
	public abstract BlockData getBlockData();
	
	@Override
	public boolean equals(Object obj) {
		Objects.requireNonNull(obj);
		if(!(obj instanceof Block))
			return false;
		Block b = (Block) obj;
		return b.getLocation().equals(getLocation()) && getType().equals(b.getType());
	}
	
	@Override
	public String toString() {
		return "Block{type=" + getType().getId() + ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() + "}";
	}

}

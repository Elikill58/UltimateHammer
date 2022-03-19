package com.elikill58.ultimatehammer.api.location;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;
import com.elikill58.ultimatehammer.api.block.Block;

public abstract class World implements UltimateHammerObject {

	/**
	 * Get the world name
	 * 
	 * @return the world name
	 */
	public abstract String getName();

	/**
	 * Get the block at the specified location on this world
	 * Return a block with AIR type if not found
	 * Can create error if world not loaded AND loading it async
	 * 
	 * @param x The X block location
	 * @param y The Y block location
	 * @param z The Z block location
	 * @return the founded block
	 */
	public abstract Block getBlockAt(int x, int y, int z);

	/**
	 * Get the block at the specified location on this world
	 * Return a block with AIR type if not found
	 * Can create error if world not loaded AND loading it async
	 * 
	 * @param x The X block location
	 * @param y The Y block location
	 * @param z The Z block location
	 * @return the founded block
	 */
	public Block getBlockAt(double x, double y, double z) {
		return getBlockAt((int) x, (int) y, (int) z);
	}

	/**
	 * Get the block at the specified location on this world
	 * Return a block with AIR type if not found
	 * Can create error if world not loaded AND loading it async
	 * 
	 * @param loc the block location
	 * @return the founded block
	 */
	public Block getBlockAt(Vector v) {
		return getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
	}

	/**
	 * Get the block at the specified location on this world
	 * Return a block with AIR type if not found
	 * Can create error if world not loaded AND loading it async
	 * 
	 * @param loc the block location
	 * @return the founded block
	 */
	public abstract Block getBlockAt(Location loc);
	
	/**
	 * Get the world difficulty
	 * 
	 * @return the world difficulty
	 */
	public abstract Difficulty getDifficulty();
	
	/**
	 * Get the max height of the world
	 * 
	 * @return the max height
	 */
	public abstract int getMaxHeight();
	
	/**
	 * Get the min height of the world
	 * 
	 * @return the min height
	 */
	public abstract int getMinHeight();
}

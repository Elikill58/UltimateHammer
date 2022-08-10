package com.elikill58.ultimatehammer.api.block;

public abstract class BlockData {

	// Leaves interface
	/**
	 * Check if leaves is persistent
	 * 
	 * @return true if persist
	 */
	public abstract boolean isLeavesPersistent();
	/**
	 * Check leave distance
	 * 
	 * @return distance
	 */
	public abstract int getLeavesDistance();

	// Farmland interface
	/**
	 * Get humidity of block
	 * 
	 * @return the humidity
	 */
	public abstract int getHumidity();
	/**
	 * Get max humidity
	 * 
	 * @return max humidity
	 */
	public abstract int getMaximumHumidity();
	
	// Ageable interface
	/**
	 * Get the age of the block
	 * 
	 * @return age of block
	 */
	public abstract int getAge();
	/**
	 * Get maximum age for this block.<br>
	 * Return -1 for old blocks or no-ageable one
	 * 
	 * @return the maximum age
	 */
	public abstract int getMaximumAge();

}

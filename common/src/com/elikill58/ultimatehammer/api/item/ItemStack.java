package com.elikill58.ultimatehammer.api.item;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;

public abstract class ItemStack implements UltimateHammerObject {

	/**
	 * Get amount of the item
	 * 
	 * @return item amount
	 */
	public abstract int getAmount();
	
	/**
	 * Get the item material type
	 * 
	 * @return the material
	 */
	public abstract Material getType();
	
	/**
	 * Get the display name of item
	 * null if the item doesn't have any name
	 * 
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * Clone the current item
	 * 
	 * @return a new instance of this item
	 */
	public abstract ItemStack clone();
	
	public abstract boolean hasEnchant(Enchantment enchant);
	public abstract int getEnchantLevel(Enchantment enchant);
	public abstract void addEnchant(Enchantment enchant, int level);
	public abstract void removeEnchant(Enchantment enchant);

	@Override
	public String toString() {
		return "ItemStack{type=" + getType().getId() + ",amount=" + getAmount() + (getName() != null ? ",name=" + getName() : "") + "}";
	}
}

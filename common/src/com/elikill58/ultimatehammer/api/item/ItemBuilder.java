package com.elikill58.ultimatehammer.api.item;

import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.colors.DyeColor;
import com.elikill58.ultimatehammer.api.entity.OfflinePlayer;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.universal.Adapter;

public abstract class ItemBuilder {

	/**
	 * Set the display name of the item
	 * If the display name is null, the item will take the basic minecraft name
	 * 
	 * @param displayName the new name
	 * @return this builder
	 */
    public abstract ItemBuilder displayName(@Nullable String displayName);

    /**
     * Reset display name.
     * Equals as {@link ItemBuilder#displayName} if you set a null name
     * 
     * @return this builder
     */
    public abstract ItemBuilder resetDisplayName();

    /**
     * Add enchant to item
     * 
     * @param enchantment the enchant name
     * @param level the level of the enchant
     * @return this builder
     */
	public abstract ItemBuilder enchant(Enchantment enchantment, int level);

    /**
     * Add enchant to item
     * 
     * @param enchantment the enchant name
     * @param level the level of the enchant
     * @return this builder
     */
	public abstract ItemBuilder itemFlag(ItemFlag... itemFlag);
	
	/**
	 * Add enchant to item without checking if it exist
	 * As essentials, this method allow you to add enchant with level at more than max allowed by default
	 * 
	 * @param enchantment the enchant name
	 * @param level the level of the enchant
	 * @return this builder
	 */
    public abstract ItemBuilder unsafeEnchant(Enchantment enchantment, int level);

	/**
	 * Add enchant to item without checking if it exist
	 * As essentials, this method allow you to add enchant with level at more than max allowed by default
	 * 
	 * @param enchantment the enchant name
	 * @param level the level of the enchant
	 * @return this builder
	 */
    public abstract ItemBuilder unsafeEnchant(String enchantment, int level);

    /**
     * Set the amount of the item.
     * Default: 1
     * 
     * @param amount the new amount of item
     * @return this builder
     */
    public abstract ItemBuilder amount(int amount);
    
    /**
     * Edit the color
     * Work only with colorable item like leather
     * 
     * @param color the new item color
     * @return this builder
     */
	public abstract ItemBuilder color(DyeColor color);

	/**
	 * Set lore to current item
	 * 
	 * @param lore the new lore list
	 * @return this builder
	 */
    public abstract ItemBuilder lore(List<String> lore);

	/**
	 * Set lore to current item
	 * 
	 * @param lore the new lore list
	 * @return this builder
	 */
    public abstract ItemBuilder lore(String... lore);

	/**
	 * Add lore to current item
	 * 
	 * @param loreToAdd Lore lines which must to be added
	 * @return this builder
	 */
    public abstract ItemBuilder addToLore(String... loreToAdd);

    public abstract void unbreakable(boolean breakable);

    /**
     * Build the item.
     * 
     * @return the usable item
     */
    public abstract ItemStack build();
	
    /**
     * Create an ItemBuilder with a material.
     * 
     * @param type the material of the item
     * @return the item builder
     */
	public static ItemBuilder Builder(Material type) {
		return Adapter.getAdapter().createItemBuilder(type);
	}
	
    /**
     * Create an ItemBuilder with a default item.
     * 
     * @param item the beginning item
     * @return the item builder
     */
	public static ItemBuilder Builder(ItemStack item) {
		return Adapter.getAdapter().createItemBuilder(item);
	}
	
	/**
	 * Create an ItemBuilder with a material's name.
	 * Compatible with "type:byte" for 1.12 and less items
	 * 
	 * @param type the type descriptor for the desired material
	 * @return the item builder
	 */
	public static ItemBuilder Builder(String type) {
		return Adapter.getAdapter().createItemBuilder(type);
	}
	
	/**
	 * Create an ItemBuilder for SkullItem
	 * 
	 * @param owner the owner of the skull
	 * @return the builder for skull item
	 */
	public static ItemBuilder Builder(Player owner) {
		return Adapter.getAdapter().createSkullItemBuilder(owner);
	}
	
	/**
	 * Create an ItemBuilder for SkullItem
	 * 
	 * @param owner the owner of the skull
	 * @return the builder for skull item
	 */
	public static ItemBuilder Builder(OfflinePlayer owner) {
		return Adapter.getAdapter().createSkullItemBuilder(owner);
	}
}

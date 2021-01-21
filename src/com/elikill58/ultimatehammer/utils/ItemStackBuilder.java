package com.elikill58.ultimatehammer.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBuilder {

    protected ItemStack itemStack;
    protected ItemMeta itemMeta;

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.hasItemMeta() ? this.itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.itemStack.getType());
    }
    
    public ItemStackBuilder(Material material) { this(new ItemStack(material)); }

    public ItemStackBuilder displayName(@Nullable String displayName) {
        this.itemMeta.setDisplayName(ChatColor.RESET + Utils.coloredMessage(displayName));
        return this;
    }

    public ItemStackBuilder unsafeEnchant(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStackBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder durability(short durability) {
    	Utils.setDurability(itemStack, durability);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean unbreakable) {
    	Object objToUnbreak = this.itemMeta;
    	if(!Version.getVersion().isNewerThan(Version.V1_13)) {
	    	try {
	    		Method m = this.itemMeta.getClass().getDeclaredMethod("spigot");
	    		m.setAccessible(true);
	    		objToUnbreak = m.invoke(this.itemMeta);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    	try {
    		Method m = objToUnbreak.getClass().getDeclaredMethod("setUnbreakable", boolean.class);
    		m.setAccessible(true);
    		m.invoke(objToUnbreak, unbreakable);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        List<String> list = this.itemMeta.hasLore() ? this.itemMeta.getLore() : new ArrayList<>();
    	for(String s : lore)
    		for(String temp : s.split("\\n"))
        		for(String tt : temp.split("/n"))
        			list.add(Utils.coloredMessage(tt));
        this.itemMeta.setLore(list);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);

        return this.itemStack;
    }
}

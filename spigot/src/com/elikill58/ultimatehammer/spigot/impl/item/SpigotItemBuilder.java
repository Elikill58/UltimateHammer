package com.elikill58.ultimatehammer.spigot.impl.item;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.colors.ChatColor;
import com.elikill58.ultimatehammer.api.colors.DyeColor;
import com.elikill58.ultimatehammer.api.entity.OfflinePlayer;
import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemBuilder;
import com.elikill58.ultimatehammer.api.item.ItemFlag;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.utils.Utils;
import com.elikill58.ultimatehammer.universal.Version;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")
public class SpigotItemBuilder extends ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

	public SpigotItemBuilder(Material type) {
    	this.itemStack = new ItemStack((org.bukkit.Material) type.getDefault());
    	byte damage = ((SpigotMaterial) type).getDamage();
    	this.itemMeta = (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    	if(!Version.getVersion().isNewerOrEquals(Version.V1_13) && damage > 0) {
    		if(!Version.getVersion().equals(Version.V1_7) && itemMeta instanceof BannerMeta)
    			((BannerMeta) this.itemMeta).setBaseColor(org.bukkit.DyeColor.getByDyeData(damage));
    		else
    			this.itemStack.setDurability(damage);
    	}
    }

	public SpigotItemBuilder(com.elikill58.ultimatehammer.api.item.ItemStack item) {
    	this.itemStack = (ItemStack) item.getDefault();
    	byte damage = ((SpigotMaterial) item.getType()).getDamage();
    	this.itemMeta = (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    	if(!Version.getVersion().isNewerOrEquals(Version.V1_13) && damage > 0) {
    		if(!Version.getVersion().equals(Version.V1_7) && itemMeta instanceof BannerMeta)
    			((BannerMeta) this.itemMeta).setBaseColor(org.bukkit.DyeColor.getByDyeData(damage));
    		else
    			this.itemStack.setDurability(damage);
    	}
    }

    public SpigotItemBuilder(String material) {
    	this.itemStack = com.elikill58.ultimatehammer.spigot.utils.Utils.getItemFromString(material);
    	this.itemMeta = (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
    }

	public SpigotItemBuilder(OfflinePlayer owner) {
    	this.itemStack = new ItemStack((org.bukkit.Material) Materials.PLAYER_HEAD.getDefault(), 1, (byte) 3);
    	SkullMeta skullmeta = (SkullMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType()));
		skullmeta.setOwner(owner.getName());
		this.itemMeta = skullmeta;
	}

	@Override
    public ItemBuilder displayName(@Nullable String displayName) {
        this.itemMeta.setDisplayName(ChatColor.RESET + Utils.coloredMessage(displayName));
        return this;
    }

    @Override
    public ItemBuilder resetDisplayName() {
        return this.displayName(null);
    }
    
    @Override
    public ItemBuilder itemFlag(ItemFlag... itemFlag) {
    	for(ItemFlag flag : itemFlag)
    		itemMeta.addItemFlags(org.bukkit.inventory.ItemFlag.valueOf(flag.name()));
    	return this;
    }

    @Override
	public ItemBuilder enchant(Enchantment enchantment, int level) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchantment);
		if(en != null)
			this.itemMeta.addEnchant(en, level, true);
        return this;
    }

    @Override
    public ItemBuilder unsafeEnchant(Enchantment enchantment, int level) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchantment);
		if(en != null)
			this.itemMeta.addEnchant(en, level, true);
        return this;
    }
    
	@Override
    public ItemBuilder unsafeEnchant(String enchantment, int level) {
    	itemMeta.addEnchant(org.bukkit.enchantments.Enchantment.getByName(enchantment), level, true);
    	return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    
    @Override
    public ItemBuilder unbreakable(boolean unbreakable) {
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
    
    @Override
	public ItemBuilder color(DyeColor color) {
		itemStack.setDurability(color.getColorFor(new SpigotMaterial(itemStack.getType())));
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			ItemMeta meta = itemStack.getItemMeta();
			((Damageable) meta).setDamage(color.getWool());
			itemStack.setItemMeta(meta);
		}
        return this;
    }

    @Override
    public ItemBuilder lore(List<String> lore) {
    	return lore(lore.toArray(new String[] {}));
    }

    @Override
    public ItemBuilder lore(String... lore) {
        List<String> list = this.itemMeta.hasLore() ? this.itemMeta.getLore() : new ArrayList<>();
    	for(String s : lore)
    		for(String temp : s.split("\\n"))
        		for(String tt : temp.split("/n"))
        			list.add(Utils.coloredMessage(tt));
        this.itemMeta.setLore(list);
        return this;
    }

    @Override
    public ItemBuilder addToLore(String... loreToAdd) {
        return lore(loreToAdd);
    }
    
    @Override
    public ItemBuilder setCustomModelData(int data) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_14)) {
			try {
				itemMeta.getClass().getDeclaredMethod("setCustomModelData", int.class).invoke(itemMeta, data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
    }

    @Override
    public com.elikill58.ultimatehammer.api.item.ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return new SpigotItemStack(itemStack);
    }
}

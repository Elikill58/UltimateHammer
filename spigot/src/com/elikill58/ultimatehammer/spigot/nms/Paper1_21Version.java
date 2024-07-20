package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.SpigotUltimateHammer;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

public class Paper1_21Version extends SpigotVersionAdapter {

	public Paper1_21Version() {
		super(Bukkit.getServer().getVersion());
	}

	private NamespacedKey getKey(String key) {
		return NamespacedKey.fromString(key, SpigotUltimateHammer.getInstance());
	}
	
	@Override
	public ItemStack setNbtTag(ItemStack item, String key, String tagVal) {
		org.bukkit.inventory.ItemStack it = ((org.bukkit.inventory.ItemStack) item.getDefault());
		ItemMeta meta = it.getItemMeta();
		PersistentDataContainer pdc = it.getItemMeta().getPersistentDataContainer();
		NamespacedKey nk = getKey(key);
		pdc.set(nk, PersistentDataType.STRING, tagVal);
		it.setItemMeta(meta);
		return new SpigotItemStack(it);
	}

	@Override
	public ItemStack setNbtTag(ItemStack item, String key, int tagVal) {
		/*org.bukkit.inventory.ItemStack it = ((org.bukkit.inventory.ItemStack) item.getDefault());
		ItemMeta meta = it.getItemMeta();
		PersistentDataContainer pdc = it.getItemMeta().getPersistentDataContainer();
		pdc.set(NamespacedKey.minecraft(key), PersistentDataType.INTEGER, tagVal);
		it.setItemMeta(meta);
		return new SpigotItemStack(it);*/
		// TODO fix and be able to change real nbt tag
		return item;
	}

	@Override
	public String getNbtTagValue(ItemStack item, String key) {
		PersistentDataContainer pdc = ((org.bukkit.inventory.ItemStack) item.getDefault()).getItemMeta().getPersistentDataContainer();
		NamespacedKey nk = getKey(key);
		if(pdc.has(nk))
			return pdc.get(nk, PersistentDataType.STRING);
		return null;
	}
}

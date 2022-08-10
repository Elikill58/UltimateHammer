package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Spigot_1_8_R3 extends SpigotVersionAdapter {

	public Spigot_1_8_R3() {
		super("v1_8_R3");
	}

	@Override
	public ItemStack setNbtTag(ItemStack item, String tagVal) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		NBTTagCompound comp = nmsItem.getTag();
		if(comp == null)
			return null;
		comp.setString(NBT_TAG_KEY, tagVal);
		nmsItem.setTag(comp);
		return new SpigotItemStack(CraftItemStack.asBukkitCopy(nmsItem));
	}

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		NBTTagCompound comp = nmsItem.getTag();
		if(comp == null)
			return false;
		String tag = comp.getString(NBT_TAG_KEY);
		return tag != null && searchedVal.equalsIgnoreCase(tag);
	}
}

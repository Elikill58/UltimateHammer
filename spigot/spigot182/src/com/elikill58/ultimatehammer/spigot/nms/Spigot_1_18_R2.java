package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

import net.minecraft.nbt.CompoundTag;

public class Spigot_1_18_R2 extends SpigotVersionAdapter {

	public Spigot_1_18_R2() {
		super("v1_18_R2");
	}

	@Override
	public ItemStack setNbtTag(ItemStack item, String tagVal) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		CompoundTag comp = nmsItem.getTag();
		if(comp == null)
			return null;
		comp.putString(NBT_TAG_KEY, tagVal);
		nmsItem.setTag(comp);
		return new SpigotItemStack(CraftItemStack.asBukkitCopy(nmsItem));
	}

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		CompoundTag comp = nmsItem.getTag();
		if(comp == null)
			return false;
		String tag = comp.getString(NBT_TAG_KEY);
		return tag != null && searchedVal.equalsIgnoreCase(tag);
	}
}

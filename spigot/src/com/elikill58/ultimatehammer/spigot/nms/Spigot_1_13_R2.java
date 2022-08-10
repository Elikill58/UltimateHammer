package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;

public class Spigot_1_13_R2 extends SpigotVersionAdapter {

	public Spigot_1_13_R2() {
		super("v1_13_R2");
	}

	@Override
	public ItemStack addNbtTag(ItemStack item, String tagVal) {
		net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		NBTTagCompound comp = nmsItem.getTag();
		if(comp == null)
			return null;
		NBTTagList list = comp.getList(NBT_TAG_KEY, 10);
		list.add(new NBTTagString(tagVal));
		comp.set(NBT_TAG_KEY, list);
		nmsItem.setTag(comp);
		return new SpigotItemStack(CraftItemStack.asBukkitCopy(nmsItem));
	}

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		NBTTagCompound comp = nmsItem.getTag();
		if(comp == null)
			return false;
		NBTTagList list = comp.getList(NBT_TAG_KEY, 10);
		for(int i = 0; i < list.size(); i++)
			if(list.getString(i).equalsIgnoreCase(searchedVal))
				return true;
		return false;
	}
}

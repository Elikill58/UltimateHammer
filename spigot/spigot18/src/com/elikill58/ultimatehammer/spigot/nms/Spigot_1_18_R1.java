package com.elikill58.ultimatehammer.spigot.nms;

import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class Spigot_1_18_R1 extends SpigotVersionAdapter {

	public Spigot_1_18_R1() {
		super("v1_18_R1");
	}

	@Override
	public ItemStack addNbtTag(ItemStack item, String tagVal) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		CompoundTag comp = nmsItem.getTag();
		if(comp == null)
			return null;
		ListTag list = comp.getList(NBT_TAG_KEY, 10);
		list.add(StringTag.valueOf(tagVal));
		comp.put(NBT_TAG_KEY, list);
		nmsItem.setTag(comp);
		return new SpigotItemStack(CraftItemStack.asBukkitCopy(nmsItem));
	}

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault());
		CompoundTag comp = nmsItem.getTag();
		if(comp == null)
			return false;
		ListTag list = comp.getList(NBT_TAG_KEY, 10);
		for(Tag st : list)
			if(st.getAsString().equalsIgnoreCase(searchedVal))
				return true;
		return false;
	}
}

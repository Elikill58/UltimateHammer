package com.elikill58.ultimatehammer.spigot.nms;

import java.lang.reflect.Method;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;
import com.elikill58.ultimatehammer.spigot.utils.PacketUtils;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Version;
import com.elikill58.ultimatehammer.universal.utils.ReflectionUtils;

public class SpigotGlobalVersion extends SpigotVersionAdapter {

	protected final Class<?> nbtTabClass = PacketUtils.getNmsClass("NBTTagCompound", "nbt."),
			craftItemStackClass = PacketUtils.getObcClass("inventory.CraftItemStack"),
			nmsItemStackClass = PacketUtils.getNmsClass("ItemStack", "world.item.");
	protected final Method setString, getString, hasKey, getTag, saveNbt;

	public SpigotGlobalVersion(String version) {
		super(version);
		Version v = Version.getVersion();
		if (v.isNewerOrEquals(Version.V1_18)) {
			setString = ReflectionUtils.getMethod(nbtTabClass, "a", String.class, String.class);
			getString = ReflectionUtils.getMethod(nbtTabClass, "a", String.class);
			hasKey = ReflectionUtils.getMethod(nbtTabClass, "e", String.class);
			getTag = ReflectionUtils.getMethod(nmsItemStackClass,
					v.equals(Version.V1_19) ? "u" : (version.equals("v1_18_R2") ? "t" : "s"));
			saveNbt = ReflectionUtils.getMethod(nmsItemStackClass, "c", nbtTabClass);
		} else {
			setString = ReflectionUtils.getMethod(nbtTabClass, "setString", String.class, String.class);
			getString = ReflectionUtils.getMethod(nbtTabClass, "getString", String.class);
			hasKey = ReflectionUtils.getMethod(nbtTabClass, "hasKey", String.class);
			getTag = ReflectionUtils.getMethod(nmsItemStackClass, "getTag");
			saveNbt = ReflectionUtils.getMethod(nmsItemStackClass, "save", nbtTabClass);
		}
	}

	@Override
	public ItemStack setNbtTag(ItemStack item, String tagVal) {
		try {
			Object nmsItem = toNMSItem(item);
			Object comp = getNBTTagCompoundFromNMSItem(nmsItem);
			setString.invoke(comp, NBT_TAG_KEY, tagVal);
			saveNbt.invoke(nmsItem, comp);
			return new SpigotItemStack((org.bukkit.inventory.ItemStack) craftItemStackClass
					.getDeclaredMethod("asBukkitCopy", nmsItemStackClass).invoke(null, nmsItem));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		try {
			Object nbtTag = getNBTTagCompound(item);
			if (nbtTag != null && (boolean) hasKey.invoke(nbtTag, NBT_TAG_KEY)) {
				Object tagVal = getString.invoke(nbtTag, NBT_TAG_KEY);
				return tagVal != null && tagVal.toString().equalsIgnoreCase(searchedVal);
			}
		} catch (Exception e) {
			Adapter.getAdapter().getLogger().info("Error " + NBT_TAG_KEY + " / " + searchedVal);
			e.printStackTrace();
		}
		return false;
	}

	private Object getNBTTagCompound(ItemStack item) throws Exception {
		return getNBTTagCompoundFromNMSItem(toNMSItem(item));
	}

	private Object getNBTTagCompoundFromNMSItem(Object nmsItem) throws Exception {
		if (nmsItem == null)
			return null;
		Object tag = getTag.invoke(nmsItem);
		return tag == null ? nbtTabClass.getConstructor().newInstance() : tag;
	}

	private Object toNMSItem(ItemStack item) throws Exception {
		return craftItemStackClass.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class).invoke(null,
				item.getDefault());
	}
}

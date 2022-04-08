package com.elikill58.ultimatehammer.utils;

import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.UltimateHammer;

public class NmsUtils {

	private static final Class<?> NBT_TAB_CLASS = PacketUtils.getNmsClass("NBTTagCompound");
	private static final Class<?> CRAFT_ITEMSTACK_CLASS = PacketUtils.getObcClass("inventory.CraftItemStack");
	private static final String NBT_TAG_KEY = "ultimatehammer";

	public static ItemStack addNbtTag(ItemStack item, String tagVal) {
		try {
			Object nmsItem = toNMSItem(item);
			Object comp = getNBTTagCompoundFromNMSItem(nmsItem);
			NBT_TAB_CLASS.getDeclaredMethod("setString", String.class, String.class).invoke(comp, NBT_TAG_KEY, tagVal);
			nmsItem.getClass().getDeclaredMethod("save", NBT_TAB_CLASS).invoke(nmsItem, comp);
			return (ItemStack) CRAFT_ITEMSTACK_CLASS.getDeclaredMethod("asBukkitCopy", nmsItem.getClass()).invoke(null,
					nmsItem);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean hasNbtTag(ItemStack item, String searchedVal) {
		try {
			Object nbtTag = getNBTTagCompound(item);
			if (nbtTag != null && (boolean) NBT_TAB_CLASS.getDeclaredMethod("hasKey", String.class).invoke(nbtTag, NBT_TAG_KEY)) {
				String val = (String) NBT_TAB_CLASS.getDeclaredMethod("getString", String.class).invoke(nbtTag, NBT_TAG_KEY);
				UltimateHammer.getInstance().getLogger().info("Tag: " + val + " / " + searchedVal);
				return val.equalsIgnoreCase(searchedVal);
			}
		} catch (Exception e) {
			UltimateHammer.getInstance().getLogger().info("Error " + NBT_TAG_KEY + " / " + searchedVal);
			e.printStackTrace();
		}
		return false;
	}

	private static Object getNBTTagCompound(ItemStack item) throws Exception {
		return getNBTTagCompoundFromNMSItem(toNMSItem(item));
	}

	private static Object getNBTTagCompoundFromNMSItem(Object nmsItem) throws Exception {
		return nmsItem == null ? null : ((boolean) nmsItem.getClass().getMethod("hasTag").invoke(nmsItem) ? nmsItem.getClass().getDeclaredMethod("getTag").invoke(nmsItem)
				: NBT_TAB_CLASS.getConstructor().newInstance());
	}

	private static Object toNMSItem(ItemStack item) throws Exception {
		return CRAFT_ITEMSTACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, item);
	}
}

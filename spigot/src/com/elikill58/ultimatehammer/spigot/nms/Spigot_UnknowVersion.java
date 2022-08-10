package com.elikill58.ultimatehammer.spigot.nms;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.SpigotUltimateHammer;
import com.elikill58.ultimatehammer.spigot.utils.PacketUtils;
import com.elikill58.ultimatehammer.universal.Adapter;

public class Spigot_UnknowVersion extends SpigotVersionAdapter {

	protected final Class<?> NBT_TAB_CLASS = PacketUtils.getNmsClass("NBTTagCompound", "nbt."), CRAFT_ITEMSTACK_CLASS = PacketUtils.getObcClass("inventory.CraftItemStack");
	
	public Spigot_UnknowVersion(String version) {
		super(version);
		SpigotUltimateHammer.getInstance().getLogger().warning("Failed to find version adapter for " + version + ".");
	}

	@Override
	public ItemStack addNbtTag(ItemStack item, String tagVal) {
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

	@Override
	public boolean hasNbtTag(ItemStack item, String searchedVal) {
		try {
			Object nbtTag = getNBTTagCompound(item);
			if (nbtTag != null
					&& (boolean) NBT_TAB_CLASS.getDeclaredMethod("hasKey", String.class).invoke(nbtTag, NBT_TAG_KEY)) {
				return ((String) NBT_TAB_CLASS.getDeclaredMethod("getString", String.class).invoke(nbtTag, NBT_TAG_KEY))
						.equalsIgnoreCase(searchedVal);
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
		Class<?> c = nmsItem.getClass();
		return ((boolean) c.getMethod("hasTag").invoke(nmsItem) ? c.getDeclaredMethod("getTag").invoke(nmsItem)
				: NBT_TAB_CLASS.getConstructor().newInstance());
	}

	private Object toNMSItem(ItemStack item) throws Exception {
		return CRAFT_ITEMSTACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, item);
	}
}

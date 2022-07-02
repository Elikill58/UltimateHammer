package com.elikill58.ultimatehammer.nms;

import org.bukkit.inventory.ItemStack;

public class NMS19 extends NMS {

	@Override
	public ItemStack addNbtTag(ItemStack item, String tagVal) {
		try {
			Object nmsItem = toNMSItem(item);
			Object comp = getNBTTagCompoundFromNMSItem(nmsItem);
			NBT_TAB_CLASS.getDeclaredMethod("a", String.class, String.class).invoke(comp, NBT_TAG_KEY, tagVal);
			nmsItem.getClass().getDeclaredMethod("b", NBT_TAB_CLASS).invoke(nmsItem, comp);
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
					&& (boolean) NBT_TAB_CLASS.getDeclaredMethod("e", String.class).invoke(nbtTag, NBT_TAG_KEY)) {
				return ((String) NBT_TAB_CLASS.getDeclaredMethod("l", String.class).invoke(nbtTag, NBT_TAG_KEY))
						.equalsIgnoreCase(searchedVal);
			}
		} catch (Exception e) {
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
		return ((boolean) c.getMethod("t").invoke(nmsItem) ? c.getDeclaredMethod("u").invoke(nmsItem)
				: NBT_TAB_CLASS.getConstructor().newInstance());
	}

	private Object toNMSItem(ItemStack item) throws Exception {
		return CRAFT_ITEMSTACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(null, item);
	}

}

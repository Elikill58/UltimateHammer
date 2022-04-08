package com.elikill58.ultimatehammer.nms;

import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.utils.PacketUtils;
import com.elikill58.ultimatehammer.utils.Version;

public abstract class NMS {

	protected static final Class<?> NBT_TAB_CLASS = PacketUtils.getNmsClass("NBTTagCompound", "nbt.");
	protected static final Class<?> CRAFT_ITEMSTACK_CLASS = PacketUtils.getObcClass("inventory.CraftItemStack");
	protected static final String NBT_TAG_KEY = "ultimatehammer";

	private static NMS nms;
	
	public static NMS getNMS() {
		if(nms == null) {
			nms = Version.getVersion().isNewerOrEquals(Version.V1_18) ? new NMS18() : new NMSDefault();
		}
		return nms;
	}
	
	public abstract ItemStack addNbtTag(ItemStack item, String tagVal);

	public abstract boolean hasNbtTag(ItemStack item, String searchedVal);
}

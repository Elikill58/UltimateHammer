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
			Version v = Version.getVersion();
			if(v.isNewerOrEquals(Version.V1_19))
				nms = new NMS19();
			else if(v.isNewerOrEquals(Version.V1_18))
				nms = new NMS18();
			else
				nms = new NMSDefault();
		}
		return nms;
	}
	
	public abstract ItemStack addNbtTag(ItemStack item, String tagVal);

	public abstract boolean hasNbtTag(ItemStack item, String searchedVal);
}

package com.elikill58.ultimatehammer.api.utils;

import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class ItemUtils {


	public static boolean hasDigSpeedEnchant(ItemStack item) {
		return item != null && item.getEnchantLevel(Enchantment.DIG_SPEED) > 2;
	}
}

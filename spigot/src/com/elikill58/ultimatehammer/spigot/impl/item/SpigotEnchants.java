package com.elikill58.ultimatehammer.spigot.impl.item;

import org.bukkit.enchantments.Enchantment;

public class SpigotEnchants {

	@SuppressWarnings("deprecation")
	public static Enchantment getEnchant(com.elikill58.ultimatehammer.api.item.Enchantment en) {
		switch (en) {
		case DIG_SPEED:
			return Enchantment.DIG_SPEED;
		case SOUL_SPEED:
			return Enchantment.getByName("SOUL_SPEED");
		case THORNS:
			return Enchantment.THORNS;
		case UNBREAKING:
			return Enchantment.DURABILITY;
		case DEPTH_STRIDER:
			return Enchantment.DEPTH_STRIDER;
		}
		return null;
	}
}

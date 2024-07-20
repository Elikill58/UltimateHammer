package com.elikill58.ultimatehammer.spigot.impl.item;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public class SpigotEnchants {

	@SuppressWarnings("deprecation")
	public static Enchantment getEnchant(com.elikill58.ultimatehammer.api.item.Enchantment en) {
		switch (en) {
		case EFFICIENCY:
			return getEnchant("EFFICIENCY", "DIG_SPEED");
		case THORNS:
			return Enchantment.THORNS;
		case UNBREAKING:
			return getEnchant("UNBREAKING", "DURABILITY");
		case DEPTH_STRIDER:
			return Enchantment.DEPTH_STRIDER;
		case BANE_OF_ARTHROPODS:
			return getEnchant("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS");
		case BINDING_CURSE:
			return Enchantment.BINDING_CURSE;
		case BLAST_PROTECTION:
			return getEnchant("BLAST_PROTECTION", "PROTECTION_EXPLOSIONS");
		case CHANNELING:
			return Enchantment.CHANNELING;
		case FEATHER_FALLING:
			return getEnchant("FEATHER_FALLING", "PROTECTION_FALL");
		case FIRE_ASPECT:
			return Enchantment.FIRE_ASPECT;
		case FIRE_PROTECTION:
			return getEnchant("FIRE_PROTECTION", "PROTECTION_FIRE");
		case FLAME:
			return getEnchant("FLAME", "ARROW_FIRE");
		case FORTUNE:
			return getEnchant("FORTUNE", "LOOT_BONUS_BLOCKS");
		case FROST_WALKER:
			return Enchantment.FROST_WALKER;
		case IMPALING:
			return Enchantment.IMPALING;
		case INFINITY:
			return getEnchant("INFINITY", "ARROW_INFINITE");
		case KNOCKBACK:
			return Enchantment.KNOCKBACK;
		case LOOTING:
			return getEnchant("LOOTING", "LOOT_BONUS_MOBS");
		case LOYALTY:
			return Enchantment.LOYALTY;
		case LUCK_OF_THE_SEA:
			return getEnchant("LUCK_OF_THE_SEA", "LUCK");
		case LURE:
			return Enchantment.LURE;
		case MENDING:
			return Enchantment.MENDING;
		case POWER:
			return getEnchant("POWER", "ARROW_DAMAGE");
		case PROJECTILE_PROTECTION:
			return getEnchant("PROJECTILE_PROTECTION", "PROTECTION_PROJECTILE");
		case PROTECTION:
			return getEnchant("PROTECTION", "PROTECTION_ENVIRONMENTAL");
		case PUNCH:
			return getEnchant("PUNCH", "ARROW_KNOCKBACK");
		case RIPTIDE:
			return Enchantment.RIPTIDE;
		case SHARPNESS:
			return getEnchant("SHARPNESS", "DAMAGE_ALL");
		case SILK_TOUCH:
			return Enchantment.SILK_TOUCH;
		case VANISHING_CURSE:
			return Enchantment.VANISHING_CURSE;
		case AQUA_AFFINITY:
			return getEnchant("AQUA_AFFINITY");
		case MULTISHOT:
		case PIERCING:
		case QUICK_CHARGE:
		case RESPIRATION:
		case SMITE:
		case SWEEPING:
		case SWIFT_SNEAK:
		case SOUL_SPEED:
			// all unknow enchant
			return Enchantment.getByKey(NamespacedKey.minecraft(en.getId().split(":")[1]));
		}
		return null;
	}
	
	public static Enchantment getEnchant(String... names) {
		for(String s : names) {
			try {
				return (Enchantment) Enchantment.class.getDeclaredField(s).get(null);
			} catch (Exception e) {} // ignore and go to next
		}
		return null;
	}
}

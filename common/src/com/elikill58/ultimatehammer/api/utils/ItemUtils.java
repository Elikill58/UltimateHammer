package com.elikill58.ultimatehammer.api.utils;

import com.elikill58.ultimatehammer.api.GameMode;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;

public class ItemUtils {

	public static void damage(UltimateTool tool, Player p, ItemStack inHand) {
		damage(tool, p, inHand, -1, 1);
	}

	public static void damage(UltimateTool tool, Player p, ItemStack inHand, int slot) {
		damage(tool, p, inHand, slot, 1);
	}

	public static void damage(UltimateTool tool, Player p, ItemStack inHand, int slot, int amount) {
		if(tool.getConfigSection().getBoolean("infinity", false) || inHand.isUnbreakable() || p.getGameMode().equals(GameMode.CREATIVE))
			return;
		short dam = 0;
		for(int i = 0; i < amount; i++) {
			boolean shouldDamage = !inHand.hasEnchant(Enchantment.UNBREAKING) || Math.random() < 1.0 / (inHand.getEnchantLevel(Enchantment.UNBREAKING) + 1);
			if (shouldDamage) {
				dam++;
			}
		}
		if(dam > 0) {
			if(inHand.getDurability() + amount > inHand.getType().getMaxDurability()) { // if item fully used
				if(slot == -1) // no slot specified
					p.setItemInHand(null);
				else
					p.getInventory().set(slot, null);
			} else
				inHand.addDamage(dam);
		}
	}
}

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
		if(tool.getConfigSection().getBoolean("infinity", false))
			return;
		short dam = 0;
		for(int i = 0; i < amount; i++) {
			if (!p.getGameMode().equals((Object) GameMode.CREATIVE)) {
				if (inHand.hasEnchant(Enchantment.DURABILITY)) {
					boolean shouldDamage = Math.random() < 1.0 / (inHand.getEnchantLevel(Enchantment.DURABILITY) + 1);
					if (shouldDamage) {
						//if(new Random().nextInt(3) == 2)
							dam++;
					}
				} else {
					//if(new Random().nextInt(3) == 2)
						dam++;
				}
			}
		}
		if(dam > 0) {
			if(getUseAmount(p, inHand) <= 0) {
				if(slot == -1)
					p.setItemInHand(null);
				else
					p.getInventory().set(slot, null);
			} else
				inHand.addDamage(dam);
		}
	}

	/**
	 * Get the amount of available use for this item.
	 * 
	 * @param p the player which are trying to use this item
	 * @param item the item
	 * @return the remaining use (or -1 in unbreakable)
	 */
	public static int getUseAmount(Player p, ItemStack item) {
		if(p.getGameMode().equals(GameMode.CREATIVE))
			return -1;
		int time = item.getType().getMaxDurability() - item.getDurability();
		if (item.hasEnchant(Enchantment.DURABILITY)) {
			for(int i = 0; i < time; i++) {
				boolean shouldNotDamage = Math.random() > 1.0 / (item.getEnchantLevel(Enchantment.DURABILITY) + 1);
				if (shouldNotDamage) {
					time++;
				}
			}
		}
		return time;
	}
}

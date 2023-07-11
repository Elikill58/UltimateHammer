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
		if(tool.getUseAmount() > 0) {
			String val = inHand.getNbtTag(UltimateTool.NBT_TAG_KEY + "-uses");
			int uses = (val == null ? 0 : Integer.parseInt(val)) + 1;
			ItemStack nextItem = uses == tool.getUseAmount() ? null : tool.parseLore(inHand.setNbtTag(UltimateTool.NBT_TAG_KEY + "-uses", String.valueOf(uses)));
			// now just set new item
			if(slot == -1) // no slot specified
				p.setItemInHand(nextItem);
			else
				p.getInventory().set(slot, nextItem);
			return;
		}
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

	public static int getAmountOfDamage(UltimateTool tool, Player p, ItemStack inHand) {
		return getAmountOfDamage(tool, p, inHand, 1);
	}
	
	/**
	 * Get amount of damage for the item according to all given param
	 * 
	 * @param tool tool of item
	 * @param p player which use item
	 * @param inHand item
	 * @param amount amount of use
	 * @return
	 */
	public static int getAmountOfDamage(UltimateTool tool, Player p, ItemStack inHand, int amount) {
		if(tool.getConfigSection().getBoolean("infinity", false) || inHand.isUnbreakable() || p.getGameMode().equals(GameMode.CREATIVE))
			return -1;
		if(tool.getUseAmount() > 0) {
			return 0;
		}
		short dam = 0;
		for(int i = 0; i < amount; i++) {
			boolean shouldDamage = !inHand.hasEnchant(Enchantment.UNBREAKING) || Math.random() < 1.0 / (inHand.getEnchantLevel(Enchantment.UNBREAKING) + 1);
			if (shouldDamage) {
				dam++;
			}
		}
		return dam;
	}
}

package com.elikill58.ultimatehammer.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.UltimateTool;

public class ItemUtils {

	public static final Material LOG = Utils.getMaterialWith1_15_Compatibility("LOG", "LEGACY_LOG");
	public static final Material LEAVES = Utils.getMaterialWith1_15_Compatibility("LEAVES", "LEGACY_LEAVES");

	public static final Material POTATO_ITEM = Utils.getMaterialWith1_15_Compatibility("POTATO_ITEM", "LEGACY_POTATO_ITEM");
	public static final Material CARROT_ITEM = Utils.getMaterialWith1_15_Compatibility("CARROT_ITEM", "LEGACY_CARROT_ITEM");
	public static final Material NETHER_WARTS = Utils.getMaterialWith1_15_Compatibility("NETHER_WARTS", "LEGACY_NETHER_WARTS");
	public static final Material SEEDS = Utils.getMaterialWith1_15_Compatibility("SEEDS", "LEGACY_SEEDS");
	public static final Material CROPS = Utils.getMaterialWith1_15_Compatibility("CROPS", "LEGACY_CROPS");

	public static final Material SOIL = Utils.getMaterialWith1_15_Compatibility("SOIL", "LEGACY_SOIL");
	public static final Material NETHER_STALK = Utils.getMaterialWith1_15_Compatibility("NETHER_STALK", "LEGACY_NETHER_STALK");


	public static void damage(UltimateTool tool, Player p, ItemStack inHand, int slot) {
		damage(tool, p, inHand, slot, 1);
	}

	public static void damage(UltimateTool tool, Player p, ItemStack inHand, int slot, int amount) {
		if(tool.getConfigSection().getBoolean("infinity", false))
			return;
		short dam = 0;
		for(int i = 0; i < amount; i++) {
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				if (inHand.getEnchantments().containsKey(Enchantment.DURABILITY)) {
					boolean shouldDamage = Math.random() < 1.0 / (inHand.getEnchantmentLevel(Enchantment.DURABILITY) + 1);
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
			if(getUseAmount(p, inHand) <= 0)
				p.getInventory().setItem(slot, null);
			else
				Utils.addDurability(inHand, dam);
		}
	}

	/**
	 * Get the amount of available use for this item.
	 * 
	 * @param p the player which are trying to use this item
	 * @param item the item
	 * @return the remaining use (or -1 in unbreakable)
	 */
	@SuppressWarnings("deprecation")
	public static int getUseAmount(Player p, ItemStack item) {
		if(p.getGameMode().equals(GameMode.CREATIVE))
			return Integer.MAX_VALUE;
		int time = item.getType().getMaxDurability() - item.getDurability();
		if (item.getEnchantments().containsKey(Enchantment.DURABILITY)) {
			for(int i = 0; i < time; i++) {
				boolean shouldNotDamage = Math.random() > 1.0 / (item.getEnchantmentLevel(Enchantment.DURABILITY) + 1);
				if (shouldNotDamage) {
					time++;
				}
			}
		}
		return time;
	}
}

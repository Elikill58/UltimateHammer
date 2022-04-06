package com.elikill58.ultimatehammer.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.UltimateTool;

public class ItemUtils {
	
	public static final Material POTATO_ITEM = getMaterialWithCompatibility("POTATO_ITEM", "POTATOES");
	public static final Material CARROT_ITEM = getMaterialWithCompatibility("CARROT_ITEM", "CARROTS");
	public static final Material NETHER_WARTS = getMaterialWithCompatibility("NETHER_WARTS", "NETHER_WART");
	public static final Material SEEDS = getMaterialWithCompatibility("SEEDS", "WHEAT_SEEDS");
	public static final Material CROPS = getMaterialWithCompatibility("CROPS", "WHEAT");

	public static final Material SOIL = getMaterialWithCompatibility("SOIL", "FARMLAND");
	public static final Material NETHER_STALK = getMaterialWithCompatibility("NETHER_STALK", "NETHER_WART");


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

	public static Material getMaterialWithCompatibility(String... tempMat) {
		for(String s : tempMat) {
			try {
				Material m = Material.getMaterial(s);
				if(m != null)
					return m;
				m = (Material) Material.class.getField(s).get(Material.class);
				if(m != null)
					return m;
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e2) {
				e2.printStackTrace();
			} catch (NoSuchFieldException e) {}
		}
		String temp = "";
		for(String s : tempMat)
			temp = temp + (temp.equalsIgnoreCase("") ? "" : ", ") + s;
		Utils.error("Failed to find Material " + temp);
		return null;
	}
}

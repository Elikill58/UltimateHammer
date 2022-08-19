package com.elikill58.ultimatehammer.spigot.impl.item;

import org.bukkit.inventory.meta.ItemMeta;

import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Version;

@SuppressWarnings("deprecation")
public class SpigotItemStack extends ItemStack {

	private final org.bukkit.inventory.ItemStack item;
	
	public SpigotItemStack(org.bukkit.inventory.ItemStack item) {
		this.item = item;
	}

	@Override
	public int getAmount() {
		return item.getAmount();
	}

	@Override
	public Material getType() {
		return Adapter.getAdapter().getItemRegistrar().get(item.getType().name());
	}

	@Override
	public String getName() {
		return item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : null;
	}

	@Override
	public boolean hasEnchant(Enchantment enchant) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchant);
		return en != null && item.containsEnchantment(en);
	}

	@Override
	public int getEnchantLevel(Enchantment enchant) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchant);
		return en == null ? 0 : item.getEnchantments().getOrDefault(en, 0);
	}

	@Override
	public void addEnchant(Enchantment enchant, int level) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchant);
		if(en != null)
			item.addUnsafeEnchantment(en, level);
	}

	@Override
	public void removeEnchant(Enchantment enchant) {
		org.bukkit.enchantments.Enchantment en = SpigotEnchants.getEnchant(enchant);
		if(en != null)
			item.removeEnchantment(en);
	}
	
	@Override
	public void setAmount(int i) {
		item.setAmount(i);	
	}
	
	@Override
	public ItemStack clone() {
		return new SpigotItemStack(item.clone());
	}

	@Override
	public Object getDefault() {
		return item;
	}

	@Override
	public int getDurability() {
		return item.getDurability();
	}

	@Override
	public void setDurability(int s) {
		item.setDurability((short) s);
	}

	@Override
	public boolean isUnbreakable() {
		try {
			if(!Version.getVersion().isNewerOrEquals(Version.V1_13)) {
		    	Object toUnbreakable = ItemMeta.class.getDeclaredMethod("spigot").invoke(item.getItemMeta());
		    	return (boolean) ItemMeta.class.getDeclaredClasses()[0].getDeclaredMethod("isUnbreakable").invoke(toUnbreakable);
			}
			return item.getItemMeta().isUnbreakable();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void setCustomModelData(int data) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_14)) {
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(data);
			item.setItemMeta(meta);
		}
	}

	@Override
	public void addDamage(short d) {
        Integer durability = item.getDurability() + d;
        if (item.getType().getMaxDurability() < durability) {
        	item.setAmount(0);
            return;
        }
        item.setDurability(durability.shortValue());
	}
}

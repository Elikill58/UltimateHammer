package com.elikill58.ultimatehammer.api.item;

import java.util.List;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.universal.Adapter;

public abstract class ItemStack implements UltimateHammerObject {

	/**
	 * Get amount of the item
	 * 
	 * @return item amount
	 */
	public abstract int getAmount();
	
	/**
	 * Get the item material type
	 * 
	 * @return the material
	 */
	public abstract Material getType();
	
	/**
	 * Get the display name of item
	 * null if the item doesn't have any name
	 * 
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * Clone the current item
	 * 
	 * @return a new instance of this item
	 */
	public abstract ItemStack clone();
	
	public abstract boolean hasEnchant(Enchantment enchant);
	public abstract int getEnchantLevel(Enchantment enchant);
	public abstract void addEnchant(Enchantment enchant, int level);
	public abstract void removeEnchant(Enchantment enchant);

	public abstract void setAmount(int i);

	public abstract boolean isSimilarExceptDamage(ItemStack compare);

	public abstract int getDurability();

	public abstract void addDamage(short d);

	@Override
	public String toString() {
		return "ItemStack{type=" + getType().getId() + ",amount=" + getAmount() + (getName() != null ? ",name=" + getName() : "") + "}";
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getItem(Configuration sec) {
		ItemBuilder builder = Adapter.getAdapter().createItemBuilder(ItemRegistrar.getInstance().get(sec.getString("material")));
		if(sec.contains("name"))
			builder.displayName(sec.getString("name"));
		if(sec.contains("lore")) {
			Object lore = sec.get("lore");
			if(lore instanceof String)
				builder.lore((String) lore);
			else if(lore instanceof List) {
				for(String s : (List<String>) lore)
					builder.lore(s);
			}
		}
		builder.amount(sec.getInt("amount", 1));
		builder.unbreakable(sec.getBoolean("unbreakable", false));
		if(sec.contains("enchant")) {
			for(String s : sec.getStringList("enchant")) {
				String enchant;
				int level = 1;
				if(s.contains(":")) {
					enchant = s.split(":")[0];
					level = Integer.parseInt(s.split(":")[1]);
				} else
					enchant = s;
				if(enchant != null)
					builder.unsafeEnchant(enchant, level);
				else
					Adapter.getAdapter().getLogger().warn("Unknow enchant " + s);
			}
		}
		return builder.build();
	}
}

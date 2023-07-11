package com.elikill58.ultimatehammer.api.item;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

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

	public abstract int getDurability();
	public abstract void setDurability(int s);

	public abstract void addDamage(short d);

	public abstract boolean isUnbreakable();
	
	public abstract void setCustomModelData(int data);
	
	public abstract List<String> getLore();
	public void setLore(String... lore) {
		setLore(Arrays.asList(lore));
	}
	public abstract void setLore(List<String> lore);
	
	public boolean hasNbtTag(String key, String val) {
		return Adapter.getAdapter().getVersionAdapter().hasNbtTagValue(this, key, val);
	}
	
	public int getNbtTagInt(String key, int def) {
		String nbt = Adapter.getAdapter().getVersionAdapter().getNbtTagValue(this, key);
		return nbt == null ? def : Integer.parseInt(nbt);
	}
	
	public @Nullable String getNbtTag(String key) {
		return Adapter.getAdapter().getVersionAdapter().getNbtTagValue(this, key);
	}
	
	/**
	 * Set nbt tag to another value and return the new item
	 * 
	 * @param key the key of nbt tag
	 * @param val the value of nbt tag
	 * @return the new item with nbt
	 */
	public ItemStack setNbtTag(String key, String val) {
		return Adapter.getAdapter().getVersionAdapter().setNbtTag(this, key, val);
	}

	@Override
	public String toString() {
		return "ItemStack{type=" + getType().getId() + ",amount=" + getAmount() + (getName() != null ? ",name=" + getName() : "") + "}";
	}
	
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
		if(sec.contains("custom_model_data"))
			builder.setCustomModelData(sec.getInt("custom_model_data"));
		if(sec.contains("flags")) {
			for(String flag : sec.getStringList("flags")) {
				try {
					builder.itemFlag(ItemFlag.valueOf(flag));
				} catch (Exception e) {
					Adapter.getAdapter().getLogger().warn("Failed to find item flag " + flag + ".");
				}
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

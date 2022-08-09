package com.elikill58.ultimatehammer.spigot.impl.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.inventory.ItemFlag;
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
	public boolean isSimilarExceptDamage(ItemStack compareCommon) {
		org.bukkit.inventory.ItemStack compare = (org.bukkit.inventory.ItemStack) compareCommon.getDefault();
		if(!Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			org.bukkit.inventory.ItemStack copiedCompare = compare.clone();
			copiedCompare.setDurability(item.getDurability());
			return item.isSimilar(copiedCompare);
		}
		// checking type
		if(item.getType() != compare.getType())
			return false;
		// checking quantity
		if(item.getAmount() != compare.getAmount())
			return false;
		if(item.getData().getData() != compare.getData().getData())
			return false;
		// checking enchant
		Map<org.bukkit.enchantments.Enchantment, Integer> mapEnchantSource = item.getEnchantments();
		Map<org.bukkit.enchantments.Enchantment, Integer> mapEnchantCompare = compare.getEnchantments();
		if(mapEnchantSource.size() != mapEnchantCompare.size())
			return false;
		for(Entry<org.bukkit.enchantments.Enchantment, Integer> entries : mapEnchantSource.entrySet()){
			org.bukkit.enchantments.Enchantment enchant = entries.getKey();
			if(!mapEnchantCompare.containsKey(enchant))
				return false;
			if(mapEnchantCompare.get(enchant) != entries.getValue())
				return false;
		}
		// checking item meta
		if(item.hasItemMeta()) {
			if(!compare.hasItemMeta())
				return false;
			ItemMeta metaSource = item.getItemMeta();
			ItemMeta metaCompare = compare.getItemMeta();
			if(metaSource.hasDisplayName() != metaCompare.hasDisplayName())
				return false;
			if(metaSource.hasDisplayName() && !metaSource.getDisplayName().equals(metaCompare.getDisplayName()))
				return false;
			List<String> loreSource = metaSource.hasLore() ? metaSource.getLore() : new ArrayList<>();
			List<String> loreCompare = metaCompare.hasLore() ? metaCompare.getLore() : new ArrayList<>();
			for(String lore : loreSource)
				if(!loreCompare.contains(lore))
					return false;
			Set<ItemFlag> flagSource = metaSource.getItemFlags();
			Set<ItemFlag> flagCompare = metaCompare.getItemFlags();
			for(ItemFlag flag : flagSource)
				if(!flagCompare.contains(flag))
					return false;
		}
		return true;
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
		return item.getItemMeta().isUnbreakable();
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

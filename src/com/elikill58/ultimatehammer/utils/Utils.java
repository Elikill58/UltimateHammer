package com.elikill58.ultimatehammer.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class Utils {

	public static String coloredMessage(String msg) {
		return ChatColor.translateAlternateColorCodes('§', msg);
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getItem(ConfigurationSection sec) {
		ItemStackBuilder builder = new ItemStackBuilder(getItemFromString(sec.getString("material")));
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
		if(sec.contains("flags")) {
			for(String flag : sec.getStringList("flags")) {
				try {
					builder.flag(ItemFlag.valueOf(flag));
				} catch (Exception e) {
					warn("Failed to find item flag " + flag + ".");
				}
			}
		}
		if(sec.contains("enchant")) {
			for(String s : sec.getStringList("enchant")) {
				Enchantment enchant;
				int level = 1;
				if(s.contains(":")) {
					enchant = Enchantment.getByName(s.split(":")[0]);
					level = Integer.parseInt(s.split(":")[1]);
				} else
					enchant = Enchantment.getByName(s);
				if(enchant != null)
					builder.unsafeEnchant(enchant, level);
				else
					warn("Unknow enchant " + s);
			}
		}
		return builder.build();
	}
	
	public static ItemStack getItemFromString(String s) {
		if(s == null) {
			warn("Error while creating item. The material is null.");
			return null;
		}
		String[] args = s.split(":");
		String id = args[0];
		try {
			Material temp = Material.valueOf(id.toUpperCase());
			if(temp != null) {
				if(args.length > 1)
					return new ItemStack(temp, 1, Byte.parseByte(args[1]));
				else
					return new ItemStack(temp);
			}
		} catch (Exception e) {}
		if(!isInteger(id))
			return null;
		byte damage = -1;
		if(args.length > 1)
			damage = Byte.parseByte(args[1]);
		Material m = getMaterialById(Integer.parseInt(id));
		if(m == null)
			return null;
		ItemStackBuilder builder = new ItemStackBuilder(m);
		if(damage != -1)
			builder.durability(damage);
		return builder.build();
	}
	
	private static Material getMaterialById(int id) {
		for(Material m : Material.values())
			if(m.getId() == id)
				return m;
		return null;
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String applyPlaceHolders(String message, String... placeholders) {
		if(message == null)
			return "";
		for (int index = 0; index <= placeholders.length - 1; index += 2)
			message = message.replaceAll(placeholders[index], placeholders[index + 1]);
		return coloredMessage(message);
	}
	
	public static void setDurability(ItemStack item, short durability) {
		item.setDurability(durability);
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			ItemMeta meta = item.getItemMeta();
			((Damageable) meta).setDamage(durability);
			item.setItemMeta(meta);
		}
	}
	
	public static void addDurability(ItemStack item, short durability) {
		item.setDurability((short) (item.getDurability() + durability));
		if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			ItemMeta meta = item.getItemMeta();
			((Damageable) meta).setDamage(((Damageable) meta).getDamage() + durability);
			item.setItemMeta(meta);
		}
	}
	
	public static ItemStack getItemInHand(Player p) {
		return p.getItemInHand();
	}
	
	public static void warn(String msg) {
		log("WARN", msg);
	}
	
	public static void error(String msg) {
		log("ERROR", msg);
	}
	
	private static void log(String type, String msg) {
		System.out.println("[UltimateHammer] [" + type + "] " + msg);
	}
	
	public static boolean foundClass(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Exception e) {}
		return false;
	}
}

package com.elikill58.ultimatehammer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
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

	static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	public static String coloredMessage(String msg) {
		return msg.replaceAll("&0", String.valueOf(ChatColor.BLACK))
				.replaceAll("&1", String.valueOf(ChatColor.DARK_BLUE))
				.replaceAll("&2", String.valueOf(ChatColor.DARK_GREEN))
				.replaceAll("&3", String.valueOf(ChatColor.DARK_AQUA))
				.replaceAll("&4", String.valueOf(ChatColor.DARK_RED))
				.replaceAll("&5", String.valueOf(ChatColor.DARK_PURPLE))
				.replaceAll("&6", String.valueOf(ChatColor.GOLD)).replaceAll("&7", String.valueOf(ChatColor.GRAY))
				.replaceAll("&8", String.valueOf(ChatColor.DARK_GRAY)).replaceAll("&9", String.valueOf(ChatColor.BLUE))
				.replaceAll("&a", String.valueOf(ChatColor.GREEN)).replaceAll("&b", String.valueOf(ChatColor.AQUA))
				.replaceAll("&c", String.valueOf(ChatColor.RED))
				.replaceAll("&d", String.valueOf(ChatColor.LIGHT_PURPLE))
				.replaceAll("&e", String.valueOf(ChatColor.YELLOW)).replaceAll("&f", String.valueOf(ChatColor.WHITE))
				.replaceAll("&k", String.valueOf(ChatColor.MAGIC)).replaceAll("&l", String.valueOf(ChatColor.BOLD))
				.replaceAll("&m", String.valueOf(ChatColor.STRIKETHROUGH))
				.replaceAll("&n", String.valueOf(ChatColor.UNDERLINE))
				.replaceAll("&o", String.valueOf(ChatColor.ITALIC)).replaceAll("&r", String.valueOf(ChatColor.RESET));
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

	public static Material getMaterialWith1_15_Compatibility(String... tempMat) {
		for(String s : tempMat) {
			try {
				Material m = (Material) Material.class.getField(s).get(Material.class);
				if(m != null)
					return m;
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e2) {
				e2.printStackTrace();
			} catch (NoSuchFieldException e) {}
		}
		String temp = "";
		for(String s : tempMat)
			temp = temp + (temp.equalsIgnoreCase("") ? "" : ", ") + s;
		error("Failed to find Material " + temp);
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
	
	public static boolean itemIsSimilar(ItemStack source, ItemStack compare) {
		if(!Version.getVersion().isNewerOrEquals(Version.V1_13)) {
			ItemStack copiedCompare = compare.clone();
			copiedCompare.setDurability(source.getDurability());
			return source.isSimilar(copiedCompare);
		}
		// checking type
		if(source.getType() != compare.getType())
			return false;
		// checking quantity
		if(source.getAmount() != compare.getAmount())
			return false;
		if(source.getData().getData() != compare.getData().getData())
			return false;
		// checking enchant
		Map<Enchantment, Integer> mapEnchantSource = source.getEnchantments();
		Map<Enchantment, Integer> mapEnchantCompare = compare.getEnchantments();
		if(mapEnchantSource.size() != mapEnchantCompare.size())
			return false;
		for(Entry<Enchantment, Integer> entries : mapEnchantSource.entrySet()){
			Enchantment enchant = entries.getKey();
			if(!mapEnchantCompare.containsKey(enchant))
				return false;
			if(mapEnchantCompare.get(enchant) != entries.getValue())
				return false;
		}
		// checking item meta
		if(source.hasItemMeta()) {
			if(!compare.hasItemMeta())
				return false;
			ItemMeta metaSource = source.getItemMeta();
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
	
	public static ItemStack getItemInHand(Player p) {
		return p.getItemInHand();
	}
	
	private static void warn(String msg) {
		log("WARN", msg);
	}
	
	private static void error(String msg) {
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

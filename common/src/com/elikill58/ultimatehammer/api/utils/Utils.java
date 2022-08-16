package com.elikill58.ultimatehammer.api.utils;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ultimatehammer.api.colors.ChatColor;
import com.elikill58.ultimatehammer.api.entity.EntityType;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.Enchantment;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class Utils {
	
	public static boolean isInBoat(Player p) {
		return p.isInsideVehicle() && p.getVehicle().getType().equals(EntityType.BOAT);
	}

	public static boolean hasThorns(Player p) {
		ItemStack[] armor = p.getInventory().getArmorContent();
		if(armor == null)
			return false;
		for(ItemStack item : armor)
			if(item != null && item.hasEnchant(Enchantment.THORNS))
				return true;
		return false;
	}

	public static String coloredMessage(String msg) {
		if(msg == null)
			return null;
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		if(msg.contains("&x§")) {
			msg = msg.replace("&x§", "§x§");
		}
		return msg;
	}

	public static List<String> coloredMessage(String... messages) {
		List<String> ret = new ArrayList<>();
		for (String message : messages) {
			ret.add(coloredMessage(message));
		}
		return ret;
	}
}

package com.elikill58.ultimatehammer.tools.hoe;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AutoPickup {
	
	public static boolean pickup(Player player, ItemStack item) {
		ArrayList<ItemStack> remaining = new ArrayList<>(giveItem(player, item).values());
		if(remaining.size() == 1) {
			ItemStack it = remaining.get(0);
			if(it == item)
				return false;
			player.getWorld().dropItem(player.getLocation(), it);
		} else {
			remaining.forEach((it) -> player.getWorld().dropItem(player.getLocation(), it));
		}
		return true;
	}
	
	private static HashMap<Integer, ItemStack> giveItem(Player p, Inventory inv, ItemStack is) {
		return inv.addItem(is);
	}

	public static HashMap<Integer, ItemStack> giveItem(Player p, ItemStack is) {
		return giveItem(p, p.getInventory(), is);
	}
}
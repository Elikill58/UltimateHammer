package com.elikill58.ultimatehammer.common.tools.hoe.pickup;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class AutoPickup {
	
	public static boolean pickup(Player player, ItemStack item) {
		// TODO re-add auto pickup
		/*ArrayList<ItemStack> remaining = new ArrayList<>(giveItem(player, item).values());
		if(remaining.size() == 1) {
			ItemStack it = remaining.get(0);
			if(it == item)
				return false;
			player.getWorld().dropItem(player.getLocation(), it);
		} else {
			remaining.forEach((it) -> player.getWorld().dropItem(player.getLocation(), it));
		}*/
		player.getInventory().addItem(item);
		return true;
	}
}

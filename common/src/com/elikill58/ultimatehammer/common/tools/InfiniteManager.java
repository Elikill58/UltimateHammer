package com.elikill58.ultimatehammer.common.tools;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.player.PlayerDeathEvent;
import com.elikill58.ultimatehammer.api.events.player.PlayerRespawnEvent;
import com.elikill58.ultimatehammer.api.inventory.PlayerInventory;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateToolType;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class InfiniteManager extends UltimateToolType implements Listeners {

	private final HashMap<UUID, List<ItemStack>> respawn = new HashMap<>();
	
	public InfiniteManager() {
		super("infinite");
	}

	@EventListener
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getPlayer();
		getTool(p).forEach((tool) -> {
			if(WorldRegionBypass.cannotBuild(p, tool, p.getLocation()))
				return;
			List<ItemStack> toRemove = e.getDrops().stream().filter(tool::isItem).collect(Collectors.toList());
			
			if(toRemove.isEmpty())
				e.getDrops().removeAll(toRemove);
			
			respawn.put(p.getUniqueId(), toRemove);
		});
	}

	@EventListener
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		List<ItemStack> items = respawn.remove(p.getUniqueId());
		if(items != null) {
			PlayerInventory inv = p.getInventory();
			for(ItemStack it : items)
				inv.addItem(it);
		}
	}
}

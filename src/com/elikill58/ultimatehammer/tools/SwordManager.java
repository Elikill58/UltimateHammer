package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.utils.Utils;

public class SwordManager extends UltimateTool implements Listener {

	private final HashMap<UUID, List<ItemStack>> respawn = new HashMap<>();
	
	public SwordManager(UltimateHammer pl) {
		super(pl, "sword");
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(!isEnabled())
			return;
		Player p = e.getEntity();
		if(WorldRegionBypass.cannotBuild(p, this, p.getLocation()))
			return;
		List<ItemStack> toRemove = new ArrayList<>();
		for(ItemStack it : e.getDrops())
			if(it.getType() == item.getType() && Utils.itemIsSimilar(it, item))
				toRemove.add(it);
		
		if(toRemove != null)
			e.getDrops().removeAll(toRemove);
		
		respawn.put(p.getUniqueId(), toRemove);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.getInventory().addItem(respawn.getOrDefault(p.getUniqueId(), new ArrayList<>()).toArray(new ItemStack[] {}));
	}
}

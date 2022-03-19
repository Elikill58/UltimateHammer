package com.elikill58.ultimatehammer.api.inventory;

import java.util.Optional;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.inventory.InventoryClickEvent;
import com.elikill58.ultimatehammer.api.inventory.AbstractInventory.UltimateHammerInventory;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;

public class InventoryManager implements Listeners {
	
	public InventoryManager() {
		
		AbstractInventory.INVENTORIES.forEach(AbstractInventory::load);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@EventListener
	public void onInventoryClick(InventoryClickEvent e) {
		PlatformHolder holder = e.getClickedInventory().getHolder();
		if(!(holder instanceof UltimateHammerHolder)) 
			return;
		UltimateHammerHolder nh = ((UltimateHammerHolder) holder).getBasicHolder();
		for(AbstractInventory inv : AbstractInventory.INVENTORIES) {
			if(inv.isInstance(nh)) {
				e.setCancelled(true);
				Player p = e.getPlayer();
				Material m = e.getCurrentItem().getType();
				if (m.equals(Materials.BARRIER)) {
					p.closeInventory();
				} else {
					inv.manageInventory(e, m, p, nh);
				}
				return;
			}
		}
	}
	
	public static Optional<AbstractInventory<?>> getInventory(UltimateHammerInventory type) {
		for(AbstractInventory<?> inv : AbstractInventory.INVENTORIES)
			if(inv.getType().equals(type))
				return Optional.of(inv);
		return Optional.empty();
	}
	
	/**
	 * Open the negativity inventory of the given type
	 * Does nothing if the inventory is not found
	 * 
	 * @param type the type of the ivnetnory which have to be showed
	 * @param p the player that have to see the inventory
	 * @param args the arguments to open the inventory
	 */
	public static void open(UltimateHammerInventory type, Player p, Object... args) {
		getInventory(type).ifPresent((inv) -> inv.openInventory(p, args));
	}
}

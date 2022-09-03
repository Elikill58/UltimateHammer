package com.elikill58.ultimatehammer.common;

import java.util.Optional;

import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.inventory.PrepareAnvilEvent;
import com.elikill58.ultimatehammer.api.events.inventory.PrepareItemCraftEvent;
import com.elikill58.ultimatehammer.api.item.ItemBuilder;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.universal.Messages;

public class InventoryFeature implements Listeners {

	@EventListener
	public void onPrepareEvent(PrepareAnvilEvent e) {
		if(UltimateTool.getAlltools().values().stream().filter(u -> !u.isRenamable() && u.isItem(e.getResult())).count() > 0) {
			e.setResult(ItemBuilder.Builder(Materials.AIR).build());
			e.getViewers().forEach(et -> Messages.sendMessage(et, "cant_edit"));
			e.setClose(true);
		}
	}
	
	@EventListener
	public void onPrepareItemCraft(PrepareItemCraftEvent e) {
		ItemStack result = e.getResult();
		if(result == null)
			return;
		for(ItemStack all : e.getMatrix()) {
			if(all == null)
				continue;
			Optional<UltimateTool> tool = UltimateTool.getTool(all);
			if(tool.isPresent() && !tool.get().isCraftable()) {
				e.setResult(null);
				return;
			}
		}
	}
}

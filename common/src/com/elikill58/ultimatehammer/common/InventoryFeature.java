package com.elikill58.ultimatehammer.common;

import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.others.PrepareAnvilEvent;
import com.elikill58.ultimatehammer.api.item.ItemBuilder;
import com.elikill58.ultimatehammer.api.item.Materials;

public class InventoryFeature implements Listeners {

	@EventListener
	public void onPrepareEvent(PrepareAnvilEvent e) {
		if(UltimateTool.getAlltools().values().stream().filter(u -> !u.isRenameble() && u.isItem(e.getResult())).count() > 0)
			e.setResult(ItemBuilder.Builder(Materials.AIR).build());
	}
}

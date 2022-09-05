package com.elikill58.ultimatehammer.spigot.support;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

public class SlimefunUsedAction implements UsedActionManager {

	@Override
	public boolean usedBreak(UltimateTool tool, Player p, ItemStack item, Block b) {
		/*BlockBreakEvent event = new BlockBreakEvent((org.bukkit.block.Block) b.getDefault(), (org.bukkit.entity.Player) p.getDefault());
		Slimefun.getRegistry().getAllSlimefunItems().forEach(sf -> Talisman.trigger(event, sf));
		return event.isCancelled();*/
		return false;
	}
	
	public static class Provider implements UsedActionManagerProvider, PluginDependentExtension {
		
		@Override
		public UsedActionManager create(Adapter adapter) {
			return new SlimefunUsedAction();
		}
		
		@Override
		public String getPluginId() {
			return "Slimefun";
		}
	}
}

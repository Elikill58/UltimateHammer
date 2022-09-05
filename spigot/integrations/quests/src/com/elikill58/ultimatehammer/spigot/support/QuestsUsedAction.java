package com.elikill58.ultimatehammer.spigot.support;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

public class QuestsUsedAction implements UsedActionManager {

	@Override
	public boolean usedBreak(UltimateTool tool, Player p, ItemStack item, Block b) {
		/*BukkitQuestsPlugin q = (BukkitQuestsPlugin) Bukkit.getPluginManager().getPlugin("Quests");
		TaskType blockBreakType = q.getTaskTypeManager().getTaskType("blockbreak");
		if(blockBreakType != null) {
			BlockBreakEvent event = new BlockBreakEvent((org.bukkit.block.Block) b.getDefault(), (org.bukkit.entity.Player) p.getDefault());
			((MiningTaskType) blockBreakType).onBlockBreak(event);
			if(event.isCancelled())
				return true;
		}
		TaskType farmingType = q.getTaskTypeManager().getTaskType("farming");
		if(farmingType != null) {
			BlockBreakEvent event = new BlockBreakEvent((org.bukkit.block.Block) b.getDefault(), (org.bukkit.entity.Player) p.getDefault());
			((FarmingTaskType) farmingType).onBlockBreak(event);
			if(event.isCancelled())
				return true;
		}*/
		return false;
	}
	
	public static class Provider implements UsedActionManagerProvider, PluginDependentExtension {
		
		@Override
		public UsedActionManager create(Adapter adapter) {
			return new QuestsUsedAction();
		}
		
		@Override
		public String getPluginId() {
			return "Quests";
		}
	}
}
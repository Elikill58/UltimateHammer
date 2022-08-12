package com.elikill58.ultimatehammer.spigot.support;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;
import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.tasktype.type.MiningTaskType;
import com.leonardobishop.quests.common.tasktype.TaskType;

public class QuestsUsedAction implements UsedActionManager {

	@Override
	public boolean usedBreak(UltimateTool tool, Player p, ItemStack item, Block b) {
		BukkitQuestsPlugin q = (BukkitQuestsPlugin) Bukkit.getPluginManager().getPlugin("Quests");
		TaskType type = q.getTaskTypeManager().getTaskType("blockbreak");
		if(type == null)
			return false;
		BlockBreakEvent event = new BlockBreakEvent((org.bukkit.block.Block) b.getDefault(), (org.bukkit.entity.Player) p.getDefault());
		((MiningTaskType) type).onBlockBreak(event);
		return event.isCancelled();
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

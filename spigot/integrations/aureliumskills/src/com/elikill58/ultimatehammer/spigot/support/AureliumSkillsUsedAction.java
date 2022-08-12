package com.elikill58.ultimatehammer.spigot.support;

import org.bukkit.event.block.BlockBreakEvent;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.foraging.ForagingLeveler;
import com.archyx.aureliumskills.skills.foraging.ForagingSource;
import com.archyx.aureliumskills.skills.mining.MiningLeveler;
import com.archyx.aureliumskills.skills.mining.MiningSource;
import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

public class AureliumSkillsUsedAction implements UsedActionManager {

	@Override
	public boolean usedBreak(UltimateTool tool, Player commonPlayer, ItemStack item, Block commonBlock) {
		org.bukkit.entity.Player p = (org.bukkit.entity.Player) commonPlayer.getDefault();
		org.bukkit.block.Block b = (org.bukkit.block.Block) commonBlock.getDefault();
		BlockBreakEvent event = new BlockBreakEvent(b, p);
		for(MiningSource source : MiningSource.values()) {
			if(source.isMatch(b)) {
				new MiningLeveler(AureliumAPI.getPlugin()).onBlockBreak(event);
				return event.isCancelled();
			}
		}
		for(ForagingSource source : ForagingSource.values()) {
			if(source.isMatch(b)) {
				new ForagingLeveler(AureliumAPI.getPlugin()).onBlockBreak(event);
				return event.isCancelled();
			}
		}
		return false;
	}

	public static class Provider implements UsedActionManagerProvider, PluginDependentExtension {

		@Override
		public UsedActionManager create(Adapter adapter) {
			return new AureliumSkillsUsedAction();
		}

		@Override
		public String getPluginId() {
			return "AureliumSkills";
		}
	}
}

package com.elikill58.ultimatehammer.spigot.support;

import com.archyx.aureliumskills.ability.Ability;
import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.configuration.Option;
import com.archyx.aureliumskills.configuration.OptionL;
import com.archyx.aureliumskills.leveler.SkillLeveler;
import com.archyx.aureliumskills.skills.Skills;
import com.archyx.aureliumskills.skills.mining.MiningAbilities;
import com.archyx.aureliumskills.skills.mining.MiningSource;
import com.archyx.aureliumskills.source.SourceTag;
import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

public class AureliumSkillsUsedAction extends SkillLeveler implements UsedActionManager {

	private final MiningAbilities miningAbilities;

	public AureliumSkillsUsedAction() {
		super(AureliumAPI.getPlugin(), Ability.MINER);
		this.miningAbilities = new MiningAbilities(AureliumAPI.getPlugin());
	}

	@Override
	public boolean usedBreak(UltimateTool tool, Player commonPlayer, Block commonBlock) {
		org.bukkit.entity.Player p = (org.bukkit.entity.Player) commonPlayer.getDefault();
		org.bukkit.block.Block b = (org.bukkit.block.Block) commonBlock.getDefault();
		if (OptionL.isEnabled(Skills.MINING)) {
			// Check block replace
			if (OptionL.getBoolean(Option.CHECK_BLOCK_REPLACE) && plugin.getRegionManager().isPlacedBlock(b)) {
				return false;
			}
			if (blockXpGainLocation(b.getLocation(), p) || blockXpGainPlayer(p))
				return false;

			// Search through sources until a match is found for the block broken
			for (MiningSource source : MiningSource.values()) {
				// Add XP to player if matched
				if (!source.isMatch(b))
					continue;
				// Check silk touch
				if (source.requiresSilkTouch()) {// && !player.getItemInHand().hasEnchant(Enchantment.)) {
					return false;
				}
				plugin.getLeveler().addXp(p, Skills.MINING, getXp(p, source));
				// Apply abilities if has tag
				if (hasTag(source, SourceTag.LUCKY_MINER_APPLICABLE)) { // don't check drop item -> always does
					miningAbilities.luckyMiner(p, b, source);
				}
				break; // Stop searching if matched
			}
			// Check custom blocks
			checkCustomBlocks(p, b, Skills.MINING);
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

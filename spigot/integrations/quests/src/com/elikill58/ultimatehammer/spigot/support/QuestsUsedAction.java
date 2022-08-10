package com.elikill58.ultimatehammer.spigot.support;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;
import com.leonardobishop.quests.common.quest.Task;

public class QuestsUsedAction implements UsedActionManager {

	/*@Override
	public boolean usedBreak(UltimateTool tool, Player p, Block b) {
		BukkitQuestsPlugin q = (BukkitQuestsPlugin) Bukkit.getPluginManager().getPlugin("Quests");
		QPlayer qPlayer = q.getPlayerManager().getPlayer(p.getUniqueId());
		
		String blockAction = "blockbreak";
		TaskType taskTypeBlockAction = q.getTaskTypeManager().getTaskType(blockAction);
		if(taskTypeBlockAction != null) {
			for (Quest quest : taskTypeBlockAction.getRegisteredQuests()) { // iterate through all quests which are registered to use this task type
	            if (qPlayer.hasStartedQuest(quest)) {
	                QuestProgress questProgress = qPlayer.getQuestProgressFile().getQuestProgress(quest);

	                for (Task task : quest.getTasksOfType(blockAction)) { // get all tasks of this type
	                    if (!TaskUtils.validateWorld((org.bukkit.entity.Player) p.getDefault(), task)) continue;

	                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId()); // get the task progress and increment progress by 1

	                    if (taskProgress.isCompleted()) { // dont need to increment a completed task
	                        continue;
	                    }

	                    int brokenBlocksNeeded = (int) task.getConfigValue("amount"); // this will retrieve a value from the config under the key "value"

	                    int progressBlocksBroken;
	                    if (taskProgress.getProgress() == null) { // note: if the player has never progressed before, getProgress() will return null
	                        progressBlocksBroken = 0;
	                    } else {
	                        progressBlocksBroken = (int) taskProgress.getProgress();
	                    }

	                    taskProgress.setProgress(progressBlocksBroken + 1); // the progress does not have to be an int, although must be serializable by the yaml provider

	                    if (((int) taskProgress.getProgress()) >= brokenBlocksNeeded) { // completion statement, if true the task is complete
	                        taskProgress.setCompleted(true);
	                    }
	                }
	            }
	        }
		}

        if(b != null) {
			String blockActionCertain = "blockbreakcertain";
			TaskType taskTypeBlockActionCertain = q.getTaskTypeManager().getTaskType(blockActionCertain);
			if(taskTypeBlockActionCertain != null) {
				for (Quest quest : taskTypeBlockActionCertain.getRegisteredQuests()) { // iterate through all quests which are registered to use this task type
		            if (qPlayer.hasStartedQuest(quest)) {
		                QuestProgress questProgress = qPlayer.getQuestProgressFile().getQuestProgress(quest);

		                for (Task task : quest.getTasksOfType(blockActionCertain)) { // get all tasks of this type
		                    if (!TaskUtils.validateWorld((org.bukkit.entity.Player) p.getDefault(), task)) continue;

		                    TaskProgress taskProgress = questProgress.getTaskProgress(task.getId());

		                    if (taskProgress.isCompleted()) {
		                        continue;
		                    }

		                    if (matchBlock(task, b)) {
		                        boolean coreProtectEnabled = (boolean) task.getConfigValue("check-coreprotect", false);
		                        int coreProtectTime = (int) task.getConfigValue("check-coreprotect-time", 3600);

		                        if (coreProtectEnabled && q.getCoreProtectHook().checkBlock((org.bukkit.block.Block) b.getDefault(), coreProtectTime)) {
		                            continue;
		                        }
		                        int brokenBlocksNeeded = (int) task.getConfigValue("amount");

		                        int progressBlocksBroken;
		                        if (taskProgress.getProgress() == null) {
		                            progressBlocksBroken = 0;
		                        } else {
		                            progressBlocksBroken = (int) taskProgress.getProgress();
		                        }

		                        taskProgress.setProgress(progressBlocksBroken + 1);

		                        if (((int) taskProgress.getProgress()) >= brokenBlocksNeeded) {
		                            taskProgress.setCompleted(true);
		                        }
		                    }
		                }
		            }
		        }
			}
        }
		return false;
	}*/

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean matchBlock(Task task, Block block) {
        Material material;

        Object configBlock = task.getConfigValues().containsKey("block") ? task.getConfigValue("block") : task.getConfigValue("blocks");
        Object configData = task.getConfigValue("data");

        List<String> checkBlocks = new ArrayList<>();
        if (configBlock instanceof List) {
            checkBlocks.addAll((List) configBlock);
        } else {
            checkBlocks.add(String.valueOf(configBlock));
        }

        for (String materialName : checkBlocks) {
            // LOG:1 LOG:2 LOG should all be supported with this
            String[] split = materialName.split(":");
            int comparableData = 0;
            if (configData != null) {
                comparableData = (int) configData;
            }
            if (split.length > 1) {
                comparableData = Integer.parseInt(split[1]);
            }

            material = Material.getMaterial(String.valueOf(split[0]));
            Material blockType = (Material) block.getType().getDefault();

            short blockData = block.getData();

            if (blockType == material) {
            	if (((split.length == 1 && configData == null) || ((int) blockData) == comparableData))
                	return true;
            }
        }
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

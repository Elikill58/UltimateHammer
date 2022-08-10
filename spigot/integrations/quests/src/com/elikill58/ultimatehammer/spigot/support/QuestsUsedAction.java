package com.elikill58.ultimatehammer.spigot.support;

import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

public class QuestsUsedAction implements UsedActionManager {

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

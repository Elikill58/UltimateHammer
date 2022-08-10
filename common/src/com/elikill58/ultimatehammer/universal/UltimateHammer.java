package com.elikill58.ultimatehammer.universal;

import java.util.HashMap;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;
import com.elikill58.ultimatehammer.universal.file.FileSaverTimer;
import com.elikill58.ultimatehammer.universal.permissions.Perm;
import com.elikill58.ultimatehammer.universal.storage.accounts.UltimateHammerAccountStorage;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class UltimateHammer {
	
	private static final HashMap<String, UltimateTool> ALL_TOOLS = new HashMap<>();
	public static HashMap<String, UltimateTool> getAlltools() {
		return ALL_TOOLS;
	}
	
	private static ScheduledTask fileSaverTimer;
	
	/**
	 * Load all Negativity's class and content.
	 * Must to be run after setting adapter
	 */
	public static void loadUltimateHammer() {
		Adapter ada = Adapter.getAdapter();
		ada.getLogger().info("Thanks for buying UltimateHammer <3");
		ada.reloadConfig();
		
		DefaultConfigValue.init();
		Database.init();
		Perm.init();
		TranslatedMessages.init();
		UltimateHammerAccountStorage.init();
		EventManager.load();
		if(!ada.getPlatformID().isProxy()) {
			FileSaverTimer old = FileSaverTimer.getInstance();
			if(old != null)
				old.runAll();
			else
				ada.getScheduler().runRepeatingAsync(new FileSaverTimer(), 20);
			if(fileSaverTimer != null)
				fileSaverTimer.cancel();
			fileSaverTimer = ada.getScheduler().runRepeatingAsync(FileSaverTimer.getInstance(), 20, "UltimateHammer FileSaver");
			WorldRegionBypass.load();
			ALL_TOOLS.clear();

			int enabled = 0;
			Configuration itemConfig = ada.getConfig().getSection("items");
			for(String key : itemConfig.getKeys()) {
				UltimateTool tool = new UltimateTool(itemConfig.getSection(key), key);
				if(tool.isEnabled())
					enabled++;
				ALL_TOOLS.put(key, tool);
			}
			ada.getLogger().info("Loaded " + ALL_TOOLS.size() + " tools (" + enabled + " enabled).");
			
		}
		UniversalUtils.init();
	}
	
	public static void disableUltimateHammer() {
		if(fileSaverTimer != null)
			fileSaverTimer.cancel();
		UltimateHammerPlayer.getAllPlayers().clear();
		Database.close();
	}
}

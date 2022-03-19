package com.elikill58.ultimatehammer.universal;

import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.universal.dataStorage.UltimateHammerAccountStorage;
import com.elikill58.ultimatehammer.universal.file.FileSaverTimer;
import com.elikill58.ultimatehammer.universal.permissions.Perm;
import com.elikill58.ultimatehammer.universal.utils.SemVer;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class UltimateHammer {
	
	/**
	 * Load all Negativity's class and content.
	 * Must to be run after setting adapter
	 */
	public static void loadNegativity() {
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
		}
		UniversalUtils.init();

		new Thread(() -> {
			SemVer latestVersion = UniversalUtils.getLatestVersionIfNewer();
			if (latestVersion != null) {
				ada.getLogger().info("New version of Negativity available: " + latestVersion.toFormattedString() + ". Download it here: https://www.spigotmc.org/resources/86874/");
			}
		}).start();
	}
}

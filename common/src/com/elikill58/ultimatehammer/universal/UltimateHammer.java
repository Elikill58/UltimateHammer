package com.elikill58.ultimatehammer.universal;

import java.util.Collections;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Predicate;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.events.EventManager;
import com.elikill58.ultimatehammer.api.plugin.ExternalPlugin;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;
import com.elikill58.ultimatehammer.universal.file.FileSaverTimer;
import com.elikill58.ultimatehammer.universal.permissions.Perm;
import com.elikill58.ultimatehammer.universal.storage.accounts.UltimateHammerAccountStorage;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class UltimateHammer {
	
	private static final Set<String> integratedPlugins = Collections.synchronizedSet(new HashSet<>());
	
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
			if(fileSaverTimer != null)
				fileSaverTimer.cancel();
			fileSaverTimer = ada.getScheduler().runRepeatingAsync(FileSaverTimer.getInstance(), 20, "UltimateHammer FileSaver");
			WorldRegionBypass.load();
			
			UltimateTool.init();
		}
		UniversalUtils.init();

		if (!integratedPlugins.isEmpty()) {
			ada.getLogger().info("Loaded support for " + String.join(", ", integratedPlugins) + ".");
		}
	}
	
	public static void disableUltimateHammer() {
		if(fileSaverTimer != null)
			fileSaverTimer.cancel();
		UltimateHammerPlayer.getAllPlayers().clear();
		Database.close();
	}
	
	public static <T> void loadExtensions(Class<T> extensionClass, Predicate<T> extensionConsumer) {
		Adapter adapter = Adapter.getAdapter();
		// First load extensions from negativity
		safelyLoadExtensions(extensionClass, UltimateHammer.class.getClassLoader(), extensionConsumer, adapter);
		// Then those from dependent plugins
		for (ExternalPlugin plugin : adapter.getDependentPlugins()) {
			ClassLoader pluginClassLoader = plugin.getDefault().getClass().getClassLoader();
			safelyLoadExtensions(extensionClass, pluginClassLoader, extensionConsumer, adapter);
		}
	}
	
	private static <T> void safelyLoadExtensions(Class<T> extensionClass, ClassLoader classLoader, Predicate<T> extensionConsumer, Adapter adapter) {
		for (T extension : ServiceLoader.load(extensionClass, classLoader)) {
			try {
				if (extension instanceof PlatformDependentExtension
					&& !((PlatformDependentExtension) extension).getPlatforms().contains(adapter.getPlatformID())) {
					continue;
				}
				
				String dependencyPluginId = null;
				if (extension instanceof PluginDependentExtension) {
					PluginDependentExtension depExt = (PluginDependentExtension) extension;
					dependencyPluginId = depExt.getPluginId();
					if (!adapter.hasPlugin(dependencyPluginId) || !depExt.hasPreRequises()) {
						continue;
					}
				}
				
				if (extensionConsumer.test(extension) && dependencyPluginId != null) {
					integratedPlugins.add(dependencyPluginId);
				}
			} catch (Throwable e) {
				adapter.getLogger().error("Failed to consume extension " + extension);
				e.printStackTrace();
			}
		}
	}
}

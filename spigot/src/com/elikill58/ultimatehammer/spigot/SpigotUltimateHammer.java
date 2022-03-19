package com.elikill58.ultimatehammer.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.spigot.listeners.BlockListeners;
import com.elikill58.ultimatehammer.spigot.listeners.CommandsListeners;
import com.elikill58.ultimatehammer.spigot.listeners.EntityListeners;
import com.elikill58.ultimatehammer.spigot.listeners.InventoryListeners;
import com.elikill58.ultimatehammer.spigot.listeners.PlayersListeners;
import com.elikill58.ultimatehammer.spigot.nms.SpigotVersionAdapter;
import com.elikill58.ultimatehammer.spigot.utils.Utils;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Database;
import com.elikill58.ultimatehammer.universal.Stats;
import com.elikill58.ultimatehammer.universal.Stats.StatsType;
import com.elikill58.ultimatehammer.universal.UltimateHammer;
import com.elikill58.ultimatehammer.universal.Version;
import com.elikill58.ultimatehammer.universal.dataStorage.UltimateHammerAccountStorage;

public class SpigotUltimateHammer extends JavaPlugin {

	private static SpigotUltimateHammer INSTANCE;
	public static boolean isCraftBukkit = false;
		
	@Override
	public void onEnable() {
		INSTANCE = this;
		
		if (!new File(getDataFolder().getAbsolutePath(), "config.yml").exists()) {
			// show message before setting adapter (which create config file)
			getLogger().info("------ UltimateHammer Information ------");
			getLogger().info("");
			getLogger().info(" > Thanks for downloading UltimateHammer :)");
			getLogger().info("I'm trying to make the better hammer has possible.");
			getLogger().info("If you get error or just have suggestion, you can contact me via:");
			getLogger().info("Discord: @Elikill58#0743, @Elikill58 on twitter or in all other web site like Spigotmc ...");
			getLogger().info("");
			getLogger().info("------ Negativity Information ------");
		}
		if (Adapter.getAdapter() == null)
			Adapter.setAdapter(new SpigotAdapter(this));
		
		Version v = Version.getVersion(Utils.VERSION);
		if (v.equals(Version.HIGHER))
			getLogger().warning("Unknow server version " + Utils.VERSION + " ! Some problems can appears.");
		else {
			SpigotVersionAdapter.getVersionAdapter();
			getLogger().info("Detected server version: " + v.name().toLowerCase(Locale.ROOT) + " (" + Utils.VERSION + ")");
		}
		getLogger().info("Running with Java " + System.getProperty("java.version"));
		
		try {
			Class.forName("org.spigotmc.SpigotConfig");
			isCraftBukkit = false;
		} catch (ClassNotFoundException e) {
			isCraftBukkit = true;
		}
		UltimateHammer.loadNegativity();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayersListeners(), this);
		pm.registerEvents(new InventoryListeners(), this);
		pm.registerEvents(new BlockListeners(), this);
		pm.registerEvents(new EntityListeners(), this);
		pm.registerEvents(new CommandsListeners(), this);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		loadCommand();
		
		Stats.sendStartupStats(Bukkit.getServer().getPort());
		
		UltimateHammerAccountStorage.setDefaultStorage("file");
	}

	private void loadCommand() {
		CommandsListeners command = new CommandsListeners();
		PluginCommand negativity = getCommand("negativity");
		negativity.setExecutor(command);
		negativity.setTabCompleter(command);
	}

	@Override
	public void onDisable() {
		new Thread(() -> new ArrayList<>(UltimateHammerPlayer.getAllPlayers().keySet()).forEach(UltimateHammerPlayer::removeFromCache)).start();
		Database.close();
		Stats.updateStats(StatsType.ONLINE, 0 + "");
	}

	public static SpigotUltimateHammer getInstance() {
		return INSTANCE;
	}
}

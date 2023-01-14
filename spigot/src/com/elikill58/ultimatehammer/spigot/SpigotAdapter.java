package com.elikill58.ultimatehammer.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.OfflinePlayer;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.inventory.UltimateHammerHolder;
import com.elikill58.ultimatehammer.api.item.ItemBuilder;
import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.nms.VersionAdapter;
import com.elikill58.ultimatehammer.api.plugin.ExternalPlugin;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotEntityManager;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotOfflinePlayer;
import com.elikill58.ultimatehammer.spigot.impl.entity.SpigotPlayer;
import com.elikill58.ultimatehammer.spigot.impl.inventory.SpigotInventory;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemBuilder;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemRegistrar;
import com.elikill58.ultimatehammer.spigot.impl.plugin.SpigotExternalPlugin;
import com.elikill58.ultimatehammer.spigot.nms.SpigotVersionAdapter;
import com.elikill58.ultimatehammer.spigot.utils.Utils;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Platform;
import com.elikill58.ultimatehammer.universal.Scheduler;
import com.elikill58.ultimatehammer.universal.Version;
import com.elikill58.ultimatehammer.universal.logger.JavaLoggerAdapter;
import com.elikill58.ultimatehammer.universal.logger.LoggerAdapter;
import com.elikill58.ultimatehammer.universal.translation.TranslationProviderFactory;
import com.elikill58.ultimatehammer.universal.translation.UltimateHammerTranslationProviderFactory;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class SpigotAdapter extends Adapter {

	private final JavaPlugin pl;
	private final TranslationProviderFactory translationProviderFactory;
	private final LoggerAdapter logger;
	private final SpigotItemRegistrar itemRegistrar;
	private final Version serverVersion;
	private final Scheduler scheduler;
	private Configuration config;

	public SpigotAdapter(JavaPlugin pl) {
		this.pl = pl;
		this.config = UniversalUtils.loadConfig(new File(pl.getDataFolder(), "config.yml"), "config.yml");
		this.translationProviderFactory = new UltimateHammerTranslationProviderFactory(
				pl.getDataFolder().toPath().resolve("lang"), "UltimateHammer");
		this.logger = new JavaLoggerAdapter(pl.getLogger());
		this.itemRegistrar = new SpigotItemRegistrar();
		this.serverVersion = Version.getVersion(getVersion());
		this.scheduler = new SpigotScheduler(pl);
	}
	
	@Override
	public Platform getPlatformID() {
		return Platform.SPIGOT;
	}

	@Override
	public Configuration getConfig() {
		return config;
	}

	@Override
	public File getDataFolder() {
		return pl.getDataFolder();
	}

	@Override
	public void debug(String msg) {
		if (getConfig().getBoolean("debug", false))
			pl.getLogger().info("[Debug] " + msg);
	}

	@Override
	public TranslationProviderFactory getPlatformTranslationProviderFactory() {
		return this.translationProviderFactory;
	}

	@Override
	public void reload() {
		reloadConfig();
	}

	@Override
	public String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	@Override
	public Version getServerVersion() {
		return this.serverVersion;
	}
	
	@Override
	public String getPluginVersion() {
		return pl.getDescription().getVersion();
	}

	@Override
	public void reloadConfig() {
		this.config = UniversalUtils.loadConfig(new File(pl.getDataFolder(), "config.yml"), "config.yml");
	}

	@Override
	public void runConsoleCommand(String cmd) {
		Bukkit.getScheduler().callSyncMethod(pl, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
	}

	@Override
	public List<UUID> getOnlinePlayersUUID() {
		List<UUID> list = new ArrayList<>();
		for (org.bukkit.entity.Player temp : Utils.getOnlinePlayers())
			list.add(temp.getUniqueId());
		return list;
	}

	@Override
	public List<Player> getOnlinePlayers() {
		List<Player> list = new ArrayList<>();
		for (org.bukkit.entity.Player temp : Utils.getOnlinePlayers())
			list.add(UltimateHammerPlayer.getUltimateHammerPlayer(temp.getUniqueId(), () -> new SpigotPlayer(temp)).getPlayer());
		return list;
	}

	@Override
	public LoggerAdapter getLogger() {
		return logger;
	}

	@Override
	public ItemRegistrar getItemRegistrar() {
		return itemRegistrar;
	}
	
	@Override
	public Inventory createInventory(String inventoryName, int size, UltimateHammerHolder holder) {
		return new SpigotInventory(inventoryName, size, holder);
	}

	@Override
	public ItemBuilder createItemBuilder(Material type) {
		return new SpigotItemBuilder(type);
	}
	
	@Override
	public ItemBuilder createItemBuilder(ItemStack item) {
		return new SpigotItemBuilder(item);
	}
	
	@Override
	public ItemBuilder createItemBuilder(String type) {
		return new SpigotItemBuilder(type);
	}
	
	@Override
	public ItemBuilder createSkullItemBuilder(Player owner) {
		return new SpigotItemBuilder(owner);
	}
	
	@Override
	public ItemBuilder createSkullItemBuilder(OfflinePlayer owner) {
		return new SpigotItemBuilder(owner);
	}

	@Override
	public @Nullable Player getPlayer(String name) {
		return SpigotEntityManager.getPlayer(Bukkit.getPlayer(name));
	}

	@Override
	public @Nullable Player getPlayer(UUID uuid) {
		return SpigotEntityManager.getPlayer(Bukkit.getPlayer(uuid));
	}

	@SuppressWarnings("deprecation")
	@Override
	public @Nullable OfflinePlayer getOfflinePlayer(String name) {
		Player online = getPlayer(name);
		if (online != null) {
			return online;
		}
		org.bukkit.OfflinePlayer p = Bukkit.getOfflinePlayer(name);
		if (p.hasPlayedBefore()) {
			return new SpigotOfflinePlayer(p);
		}
		return null;
	}
	
	@Override
	public @Nullable OfflinePlayer getOfflinePlayer(UUID uuid) {
		Player online = getPlayer(uuid);
		if (online != null) {
			return online;
		}
		org.bukkit.OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
		if (p.hasPlayedBefore()) {
			return new SpigotOfflinePlayer(p);
		}
		return null;
	}
	
	@Override
	public void runSync(Runnable call) {
		Bukkit.getScheduler().runTask(pl, call);
	}
	
	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
	@Override
	public void broadcastMessage(String message) {
		Bukkit.broadcastMessage(message);
	}
	
	@Override
	public VersionAdapter<?> getVersionAdapter() {
		return SpigotVersionAdapter.getVersionAdapter();
	}

	@Override
	public boolean callBreakEvent(Block b, Player p) {
		org.bukkit.block.Block spigot = (org.bukkit.block.Block) b.getDefault();
		spigot.setMetadata(SpigotUltimateHammer.BLOCK_METADATA, new FixedMetadataValue(pl, true));
		BlockBreakEvent event = new BlockBreakEvent(spigot, (org.bukkit.entity.Player) p.getDefault());
		Bukkit.getPluginManager().callEvent(event);
		spigot.removeMetadata(SpigotUltimateHammer.BLOCK_METADATA, pl); // now remove metadata
		return event.isCancelled();
	}
	
	@Override
	public boolean callLeavesDecayEvent(Block b) {
		LeavesDecayEvent event = new LeavesDecayEvent((org.bukkit.block.Block) b.getDefault());
		Bukkit.getPluginManager().callEvent(event);
		return event.isCancelled();
	}

	@Override
	public boolean hasPlugin(String id) {
		return Bukkit.getPluginManager().getPlugin(id) != null;
	}

	@Override
	public List<ExternalPlugin> getDependentPlugins() {
		return Arrays.stream(Bukkit.getPluginManager().getPlugins())
				.filter(plugin -> {
					PluginDescriptionFile description = plugin.getDescription();
					return description.getDepend().contains("Negativity") || description.getSoftDepend().contains("Negativity");
				})
				.map(SpigotExternalPlugin::new)
				.collect(Collectors.toList());
	}
}

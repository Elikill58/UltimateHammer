package com.elikill58.ultimatehammer.universal;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.entity.OfflinePlayer;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.inventory.UltimateHammerHolder;
import com.elikill58.ultimatehammer.api.item.ItemBuilder;
import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.packets.nms.VersionAdapter;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.universal.logger.LoggerAdapter;
import com.elikill58.ultimatehammer.universal.translation.TranslationProviderFactory;

public abstract class Adapter {

	private static Adapter adapter = null;

	public static void setAdapter(Adapter adapter) {
		if(Adapter.adapter != null) {
			try {
				throw new IllegalAccessException("No ! You don't must to change the Adapter !");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Adapter.adapter = adapter;
	}

	public static Adapter getAdapter() {
		return adapter;
	}
	
	/**
	 * Get the platform
	 * 
	 * @return the platform
	 */
	public abstract Platform getPlatformID();

	/**
	 * Get the platform name
	 * 
	 * @return the platform name
	 */
	public String getName() {
		return getPlatformID().getName();
	}
	
	/**
	 * Get the Negativity's config of the platform
	 * 
	 * @return the Negativity configuration
	 */
	public abstract Configuration getConfig();
	
	/**
	 * Get the data folder of the plugin
	 * 
	 * @return the data folder
	 */
	public abstract File getDataFolder();

	/**
	 * Get a logger adapter to log informations.
	 * 
	 * @return the logger
	 */
	public abstract LoggerAdapter getLogger();
	
	public abstract void debug(String msg);
	public abstract TranslationProviderFactory getPlatformTranslationProviderFactory();
	
	/**
	 * Reload Negativity's plugin
	 */
	public abstract void reload();
	
	/**
	 * Get the platform version
	 * 
	 * @return the platform version
	 */
	public abstract String getVersion();
	
	/**
	 * The Minecraft version the server is running
	 */
	public abstract Version getServerVersion();
	
	/**
	 * Get the version of the Negativity plugin
	 * 
	 * @return the version of Negativity
	 */
	public abstract String getPluginVersion();
	
	/**
	 * Reload the configuration of Negativity
	 */
	public abstract void reloadConfig();
	
	/**
	 * Run a command from console
	 * 
	 * @param cmd the command which have to be execute
	 */
	public abstract void runConsoleCommand(String cmd);
	
	/**
	 * Get UUID of all online players
	 * 
	 * @return online players UUID
	 */
	public abstract List<UUID> getOnlinePlayersUUID();
	
	/**
	 * Get all online players
	 * 
	 * @return all online players
	 */
	public abstract List<Player> getOnlinePlayers();
	
	/**
	 * Get the registrar of all items
	 * 
	 * @return the item registrar
	 */
	public abstract ItemRegistrar getItemRegistrar();
	
	/**
	 * Create a new item builder with the given type
	 * 
	 * @param type the item type
	 * @return a new item builder of itemstack with the given type
	 */
	public abstract ItemBuilder createItemBuilder(Material type);
	
	/**
	 * Create a new item builder with the given item
	 * 
	 * @param item the beginning item
	 * @return a new item builder of itemstack with the given type
	 */
	public abstract ItemBuilder createItemBuilder(ItemStack item);
	
	/**
	 * Create a new item builder with the given type name
	 * 
	 * @param type the item type name
	 * @return a new item builder of itemstack with the given type name
	 */
	public abstract ItemBuilder createItemBuilder(String type);
	
	/**
	 * Create a new item builder of skull with the current owner
	 * 
	 * @param owner the player owner of the skull
	 * @return an item builder of the skull
	 */
	public abstract ItemBuilder createSkullItemBuilder(Player owner);
	
	/**
	 * Create a new item builder of skull with the current owner
	 * 
	 * @param owner the player owner of the skull
	 * @return an item builder of the skull
	 */
	public abstract ItemBuilder createSkullItemBuilder(OfflinePlayer owner);
	
	/**
	 * Create a new inventory
	 * 
	 * @param inventoryName the inventory name
	 * @param size the inventory size
	 * @param holder the inventory holder
	 * @return a new inventory
	 */
	public abstract Inventory createInventory(String inventoryName, int size, UltimateHammerHolder holder);
	
	/**
	 * Get the UUID with the name
	 * 
	 * @param name the name of a possible player
	 * @return the uuid of the player
	 */
	public @Nullable UUID getUUID(String name) {
		OfflinePlayer op = getOfflinePlayer(name);
		return op == null ? null : op.getUniqueId();
	}
	
	/**
	 * Get offline player with the given name
	 * Prefer use {@link #getOfflinePlayer(UUID)} because this method isn't supported on all platform, and a name can be changed
	 *
	 * @param name the offline player name
	 * @return the offline player, or {@code null} if no player matching this name has played on this server
	 */
	public abstract @Nullable OfflinePlayer getOfflinePlayer(String name);
	
	/**
	 * Get offline player with the given UUID
	 * 
	 * @param uuid the offline player UUID
	 * @return the offline player, or {@code null} if no player with this UUID has played on this server
	 */
	public abstract @Nullable OfflinePlayer getOfflinePlayer(UUID uuid);

	/**
	 * Get player with the given name
	 * Prefer use {@link #getPlayer(UUID)} because this method isn't supported on all platform, and a name can be changed
	 * 
	 * @param name the player name
	 * @return the player, or {@code null} if no player matching this name is online
	 */
	public abstract @Nullable Player getPlayer(String name);
	
	/**
	 * Get player with the given UUID
	 * 
	 * @param uuid the player UUID
	 * @return the player, or {@code null} if no player with this UUID is online
	 */
	public abstract @Nullable Player getPlayer(UUID uuid);

	/**
	 * Run action sync with the server.
	 * Specially useful for world/player action
	 * 
	 * @param call the action to call
	 */
	public abstract void runSync(Runnable call);
	
	/**
	 * @return a synchronous scheduler that can be used to schedule task on the server thread.
	 */
	public abstract Scheduler getScheduler();
	
	/**
	 * Check if can send stats
	 * 
	 * @return true is can send stats
	 */
	public boolean canSendStats() {
		return true;
	}

	/**
	 * Broadcast the message to all online player, without any filter.
	 * 
	 * @param message the message to send
	 */
	public abstract void broadcastMessage(String message);
	
	/**
	 * Get the version adapter for the actual platform version
	 * 
	 * @return the version adapter
	 */
	public abstract VersionAdapter<?> getVersionAdapter();
}

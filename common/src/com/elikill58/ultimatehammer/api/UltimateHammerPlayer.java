package com.elikill58.ultimatehammer.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;

public class UltimateHammerPlayer {

	private static final Map<UUID, UltimateHammerPlayer> PLAYERS = Collections.synchronizedMap(new ConcurrentHashMap<UUID, UltimateHammerPlayer>());

	private final UUID playerId;
	private final Player p;
	public UltimateHammerPlayer(Player p) {
		this.p = p;
		this.playerId = p.getUniqueId();
	}

	/**
	 * Get the Negativity account of the player
	 * 
	 * @return the negativity account
	 */
	public UltimateHammerAccount getAccount() {
		return null;// NegativityAccount.get(playerId);
	}
	
	/**
	 * Get the player UUID
	 * 
	 * @return the player UUID
	 */
	public UUID getUUID() {
		return playerId;
	}
	
	/**
	 * Get the player name
	 * 
	 * @return the player name
	 */
	public String getName() {
		return getPlayer().getName();
	}

	/**
	 * Get the player
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * Get the Negativity Player or create a new one
	 * 
	 * @param p the player which we are looking for it's NegativityPlayer
	 * @return the negativity player
	 */
	public static UltimateHammerPlayer getNegativityPlayer(Player p) {
		return getNegativityPlayer(p.getUniqueId(), () -> p);
	}

	/**
	 * Get the Negativity Player or create a new one
	 * 
	 * @param uuid the player UUID
	 * @param call a creator of a new player
	 * @return the negativity player
	 */
	public static UltimateHammerPlayer getNegativityPlayer(UUID uuid, Callable<Player> call) {
		synchronized (PLAYERS) {
			return PLAYERS.computeIfAbsent(uuid, (a) -> {
				try {
					return new UltimateHammerPlayer(call.call());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			});
		}
	}

	/**
	 * Get the negativity player in cache of the given UUID
	 * 
	 * @param playerId the player UUID
	 * @return the negativity player
	 */
	public static UltimateHammerPlayer getCached(UUID playerId) {
		synchronized (PLAYERS) {
			return PLAYERS.get(playerId);
		}
	}
	
	/**
	 * Get all uuid and their negativity players
	 * 
	 * @return negativity players
	 */
	public static Map<UUID, UltimateHammerPlayer> getAllPlayers(){
		synchronized (PLAYERS) {
			return PLAYERS;
		}
	}
	
	public static List<UltimateHammerPlayer> getAllNegativityPlayers() {
		synchronized (PLAYERS) {
			return new ArrayList<>(PLAYERS.values());
		}
	}

	/**
	 * Remove the player from cache
	 * 
	 * @param playerId the player UUID
	 */
	public static void removeFromCache(UUID playerId) {
		synchronized (PLAYERS) {
			PLAYERS.remove(playerId);
		}
	}
}

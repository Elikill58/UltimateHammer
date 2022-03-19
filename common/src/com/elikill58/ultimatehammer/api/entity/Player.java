package com.elikill58.ultimatehammer.api.entity;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.GameMode;
import com.elikill58.ultimatehammer.api.inventory.Inventory;
import com.elikill58.ultimatehammer.api.inventory.PlayerInventory;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.potion.PotionEffect;
import com.elikill58.ultimatehammer.api.potion.PotionEffectType;

public interface Player extends OfflinePlayer {

	/**
	 * Get the player IP
	 * 
	 * @return player IP
	 */
	@Nullable
	String getIP();
	
	/**
	 * Know if the player is dead
	 * 
	 * @return true if the player is dead
	 */
	boolean isDead();
	
	/**
	 * Know if the player is sleeping
	 * 
	 * @return true is the player is sleeping
	 */
	boolean isSleeping();
	/**
	 * Know if the player is swimming
	 * (compatible with 1.8 and lower)
	 * 
	 * @return true if it's swimming
	 */
	boolean isSwimming();
	/**
	 * Know if the player is currently using riptide effect
	 * (compatible with 1.12 and lower)
	 * 
	 * @return true if it's ripting
	 */
	boolean isUsingRiptide();
	/**
	 * Check if the player is using elytra (flying with it)
	 * (compatible with 1.8 and lower)
	 * 
	 * @return true if is elytra flying
	 */
	boolean hasElytra();
	/**
	 * Check if the player has the specified permission
	 * 
	 * @param perm the needed permission
	 * @return true if the player has permission
	 */
	boolean hasPermission(String perm);
	/**
	 * Check if player can see the specified entity
	 * 
	 * @param entity the entity to see
	 * @return true if the player can see it
	 */
	boolean hasLineOfSight(Entity entity);
	
	/**
	 * Check if the player is flying
	 * 
	 * @return true is the player fly
	 */
	boolean isFlying();
	/**
	 * Check if the player is authorized to fly
	 * 
	 * @return true if the player can fly
	 */
	boolean getAllowFlight();
	/**
	 * Edit the authorization to fly
	 * 
	 * @param b true if the player is allowed to fly
	 */
	void setAllowFlight(boolean b);

	/**
	 * Get player XP level
	 * 
	 * @return the player level
	 */
	int getLevel();
	
	/**
	 * Get player fly speed
	 * 
	 * @return the speed when player fly
	 */
	float getFlySpeed();
	/**
	 * Get player walk speed
	 * 
	 * @return the speed when player walk
	 */
	float getWalkSpeed();
	/**
	 * Get the player fall distance when player fall
	 * 
	 * @return the player fall distance
	 */
	float getFallDistance();
	
	/**
	 * Get the player health
	 * 
	 * @return the health
	 */
	double getHealth();
	
	/**
	 * Get the max player health
	 * 
	 * @return the max health
	 */
	double getMaxHealth();
	
	/**
	 * Change the player health
	 * 
	 * @param health next health
	 */
	void setHealth(double health);

	
	/**
	 * Get player gamemode
	 * 
	 * @return the Gamemode
	 */
	GameMode getGameMode();
	
	/**
	 * Set the player gamemode
	 * Warn: support only default gamemode. Not modded server.
	 * 
	 * @param gameMode the new player gamemode
	 */
	void setGameMode(GameMode gameMode);

	/**
	 * Damage player according to damage amount
	 * 
	 * @param amount the quantity of damage
	 */
	void damage(double amount);

	/**
	 * Teleport player to specified location
	 * 
	 * @param loc location destination
	 */
	void teleport(Location loc);
	/**
	 * Teleport player to specified entity
	 * 
	 * @param et entity destination
	 */
	void teleport(Entity et);

	boolean isSneaking();
	void setSneaking(boolean b);
	
	boolean isSprinting();
	void setSprinting(boolean b);

	/**
	 * Get player world
	 * 
	 * @return the world where the player is
	 */
	World getWorld();

	/**
	 * Get the entity which is used as vehicle.
	 * It can be a wagon or a zombie.
	 * 
	 * @return the vehicle entity
	 */
	Entity getVehicle();
	
	/**
	 * Check if player is in a vehicle.
	 * 
	 * @return true if it is in vehicle
	 */
	boolean isInsideVehicle();

	/**
	 * Get the item in main hand
	 * Return null if there is not any item in hand
	 * 
	 * @return the item in hand
	 */
	ItemStack getItemInHand();
	
	/**
	 * Get the item in second hand
	 * Compatible with 1.8 and lower.
	 * Return null if there is any item in second hand or if the server is on 1.8 or lower
	 * 
	 * @return the item in off hand
	 */
	ItemStack getItemInOffHand();
	
	boolean hasPotionEffect(PotionEffectType type);
	List<PotionEffect> getActivePotionEffect();
	Optional<PotionEffect> getPotionEffect(PotionEffectType type);
	default void addPotionEffect(PotionEffect pe) {
		addPotionEffect(pe.getType(), pe.getDuration(), pe.getAmplifier());
	}
	void addPotionEffect(PotionEffectType type, int duration, int amplifier);
	void removePotionEffect(PotionEffectType type);
	
	List<Entity> getNearbyEntities(double x, double y, double z);

	PlayerInventory getInventory();
	Inventory getOpenInventory();
	boolean hasOpenInventory();
	void openInventory(Inventory inv);
	void closeInventory();
	void updateInventory();

	/**
	 * Get the player address
	 * 
	 * @return the player inet address
	 */
	InetSocketAddress getAddress();
	
	static boolean isSamePlayer(Player player1, Player player2) {
		return player1.getUniqueId().equals(player2.getUniqueId());
	}
}

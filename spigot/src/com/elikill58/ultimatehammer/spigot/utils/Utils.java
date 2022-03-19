package com.elikill58.ultimatehammer.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.spigot.SpigotUltimateHammer;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotMaterial;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;
import com.google.common.base.Preconditions;

@SuppressWarnings("deprecation")
public class Utils {

	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	public static List<Player> getOnlinePlayers() {
		return new ArrayList<Player>(Bukkit.getOnlinePlayers());
	}

	@Nullable
	public static Player getFirstOnlinePlayer() {
		List<Player> onlinePlayers = getOnlinePlayers();
		return onlinePlayers.isEmpty() ? null : onlinePlayers.iterator().next();
	}
	
	public static ItemStack getItemFromString(String s) {
		Preconditions.checkNotNull(s, "Error while creating item. The material is null.");
		try {
			String[] splitted = s.toUpperCase(Locale.ROOT).split(":");
			String key = splitted[0];
			Material temp = null;
			try {
				temp = Material.valueOf(key);
			} catch (IllegalArgumentException e) {}
			if(temp == null && UniversalUtils.isInteger(key)) {
				try {
					temp = (Material) Material.class.getDeclaredMethod("getMaterial", int.class).invoke(null, Integer.parseInt(key));
				} catch (Exception e) {
					// method not found because of too recent version
				}
			}
			byte b = splitted.length > 1 ? Byte.parseByte(s.split(":")[1]) : -1;
			if(temp == null) {
				com.elikill58.ultimatehammer.api.item.Material ownMaterial = ItemRegistrar.getInstance().get(s);
				if(ownMaterial != null) {
					temp = (Material) ownMaterial.getDefault();
					b = ((SpigotMaterial) ownMaterial).getDamage();
				}
			}
			
			if(temp == null) {
				SpigotUltimateHammer.getInstance().getLogger().warning("Error while creating item. Cannot find item for " + s + ".");
				return null;
			}
			return b != -1 ? new ItemStack(temp, 1, b) : new ItemStack(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

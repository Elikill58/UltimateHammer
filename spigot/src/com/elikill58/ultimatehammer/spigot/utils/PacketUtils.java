package com.elikill58.ultimatehammer.spigot.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import com.elikill58.ultimatehammer.universal.Version;

public class PacketUtils {

	private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	public static final String NMS_PREFIX, OBC;

	public static final Class<?> CRAFT_PLAYER_CLASS, CRAFT_ENTITY_CLASS;
	
	static {
		OBC = "org.bukkit.craftbukkit." + VERSION + ".";
		NMS_PREFIX = Version.getVersion(VERSION).isNewerOrEquals(Version.V1_17) ? "net.minecraft." : "net.minecraft.server." + VERSION + ".";
		CRAFT_PLAYER_CLASS = PacketUtils.getObcClass("entity.CraftPlayer");
		CRAFT_ENTITY_CLASS = PacketUtils.getObcClass("entity.CraftEntity");
	}
	
	/**
	 * This Map is to reduce Reflection action which take more ressources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS = new HashMap<>();
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @param packagePrefix the prefix of the package for 1.17+
	 * @return the loaded or cached class
	 */
	public static Class<?> getNmsClass(String name, String packagePrefix){
		synchronized(ALL_CLASS) {
			return ALL_CLASS.computeIfAbsent(name, (s) -> {
				try {
					return Class.forName(NMS_PREFIX + (Version.getVersion(VERSION).isNewerOrEquals(Version.V1_17) ? packagePrefix : "") + name);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			});
		}
	}

	public static Object getBoundingBox(Entity p) {
		try {
			//((CraftEntity) p).getHandle().getBoundingBox();
			Object ep = CRAFT_ENTITY_CLASS.getDeclaredMethod("getHandle").invoke(CRAFT_ENTITY_CLASS.cast(p));
			if(Version.getVersion().equals(Version.V1_7))
				return getNmsClass("Entity", "world.entity.").getDeclaredField("boundingBox").get(ep);
			else if(Version.getVersion().isNewerOrEquals(Version.V1_18))
				return getNmsClass("Entity", "world.entity.").getDeclaredMethod("cw").invoke(ep);
			else
				return getNmsClass("Entity", "world.entity.").getDeclaredMethod("getBoundingBox").invoke(ep);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return clazz the searched class
	 */
	public static Class<?> getObcClass(String name){
		if(ALL_CLASS.containsKey(name))
			return ALL_CLASS.get(name);
		try {
			Class<?> clazz = Class.forName(OBC + name);
			ALL_CLASS.put(name, clazz);
			return clazz;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

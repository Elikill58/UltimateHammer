package com.elikill58.ultimatehammer.spigot.utils;

import java.util.HashMap;

import org.bukkit.entity.Entity;

import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Version;

public class PacketUtils {
	
	/**
	 * This Map is to reduce Reflection action which take more ressources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS = new HashMap<>();
	private static final boolean isNewSystem = isNewSystem();
	public static final String NMS_PREFIX = isNewSystem ? "net.minecraft." : "net.minecraft.server." + Adapter.getAdapter().getVersion() + ".";
	public static final Class<?> CRAFT_PLAYER_CLASS, CRAFT_ENTITY_CLASS;
	
	static {
		CRAFT_PLAYER_CLASS = PacketUtils.getObcClass("entity.CraftPlayer");
		CRAFT_ENTITY_CLASS = PacketUtils.getObcClass("entity.CraftEntity");
	}
	
	private static boolean isNewSystem() {
		try {
			Class.forName("net.minecraft.server." + Adapter.getAdapter().getVersion() + ".MinecraftServer");
			return false;
		} catch (Exception e) {
		}
		return true;
	}
	
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
					return Class.forName(NMS_PREFIX + (isNewSystem ? packagePrefix : "") + name);
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
		synchronized (ALL_CLASS) {
			return ALL_CLASS.computeIfAbsent(name, (s) -> {
				try {
					String version = Adapter.getAdapter().getVersion();
					return Class.forName("org.bukkit.craftbukkit." + (version.equalsIgnoreCase("") ? "" : version + ".") + name);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			});
		}
	}
}

package com.elikill58.ultimatehammer.utils;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.elikill58.ultimatehammer.UltimateHammer;

public class PacketUtils {

	private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	public static final String NMS_PREFIX, OBC;
	
	static {
		OBC = "org.bukkit.craftbukkit." + VERSION + ".";
		Version v = Version.getVersion(VERSION);
		NMS_PREFIX = v.isNewerOrEquals(Version.V1_17) ? "net.minecraft." : "net.minecraft.server." + VERSION + ".";
		UltimateHammer.getInstance().getLogger().info("Loaded packets for " + v.getName() + " (" + VERSION + ").");
	}
	
	/**
	 * This Map is to reduce Reflection action which take more ressources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS = new HashMap<>();
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return the loaded or cached class
	 */
	public static Class<?> getNmsClass(String name){
		return getNmsClass(name, "");
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
					return Class.forName(NMS_PREFIX + (Version.getVersion(VERSION).isNewerOrEquals(Version.V1_17) ? packagePrefix : "") + name);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			});
		}
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
	
	public static Method getMethodWithName(Class<?> clazz, String name) throws Exception {
		for(Method m : clazz.getDeclaredMethods()) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}
	
	public static Method getMethodThatReturn(Object obj, Class<?> returnClass, int paramAmount) throws Exception {
		for(Method m : obj.getClass().getDeclaredMethods()) {
			if(m.getReturnType().equals(returnClass) && m.getParameterCount() == paramAmount) {
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}
}

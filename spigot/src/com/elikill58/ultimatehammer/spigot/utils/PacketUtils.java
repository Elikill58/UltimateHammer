package com.elikill58.ultimatehammer.spigot.utils;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.universal.Version;

public class PacketUtils {

	private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	public static final String NMS_PREFIX, OBC;

	public static final Class<?> CRAFT_PLAYER_CLASS, CRAFT_SERVER_CLASS, CRAFT_ENTITY_CLASS;
	
	static {
		OBC = "org.bukkit.craftbukkit." + VERSION + ".";
		NMS_PREFIX = Version.getVersion(VERSION).isNewerOrEquals(Version.V1_17) ? "net.minecraft." : "net.minecraft.server." + VERSION + ".";
		CRAFT_PLAYER_CLASS = PacketUtils.getObcClass("entity.CraftPlayer");
		CRAFT_ENTITY_CLASS = PacketUtils.getObcClass("entity.CraftEntity");
		CRAFT_SERVER_CLASS = PacketUtils.getObcClass("CraftServer");
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
	 * Get the current player ping
	 *
	 * @param p the player
	 * @return the player ping
	 */
	public static int getPing(Player p) {
		try {
			Object entityPlayer = getEntityPlayer(p);
			return entityPlayer.getClass().getField("ping").getInt(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getEntityPlayer(Player p) {
		try {
			Object craftPlayer = CRAFT_PLAYER_CLASS.cast(p);
			return craftPlayer.getClass().getMethod("getHandle").invoke(craftPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getDedicatedServer() {
		try {
			Object server = CRAFT_SERVER_CLASS.cast(Bukkit.getServer());
			return server.getClass().getMethod("getServer").invoke(server);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param et the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getNMSEntity(Entity et) {
		try {
			Object craftEntity = CRAFT_ENTITY_CLASS.cast(et);
			return craftEntity.getClass().getMethod("getHandle").invoke(craftEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the NMS world server
	 * 
	 * @param loc the location in the world of which we want to get the NMS world server
	 * @return the world server of location's world
	 */
	public static Object getWorldServer(Location loc) {
		try {
			Object object = Class.forName("org.bukkit.craftbukkit." + VERSION + ".CraftWorld").cast(loc.getWorld());
			return object.getClass().getMethod("getHandle").invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getNmsEntityName(Object nmsEntity) {
		try {
			if(Version.getVersion().isNewerOrEquals(Version.V1_13)) {
				Object chatBaseComponent = getNmsClass("Entity").getDeclaredMethod("getDisplayName").invoke(nmsEntity);
				return (String) getNmsClass("IChatBaseComponent").getDeclaredMethod("getString").invoke(chatBaseComponent);
			} else {
				return (String) getNmsClass("Entity").getDeclaredMethod("getName").invoke(nmsEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
			/*Class<?> craftMonsterClass = Class.forName("org.bukkit.craftbukkit." + VERSION + ".entity.CraftEntity");
			if(cp.getClass().isInstance(craftMonsterClass)) { // prevent protected items
				Object ep = craftMonsterClass.getDeclaredMethod("getHandle").invoke(craftMonsterClass.cast(cp));
				if(Version.getVersion().equals(Version.V1_7))
					return getNmsClass("Entity").getDeclaredField("boundingBox").get(ep);
				else
					return getNmsClass("Entity").getDeclaredMethod("getBoundingBox").invoke(ep);
			} else {
				SpigotNegativity.getInstance().getLogger().info(cp.getClass().getName() + " isn't " + craftMonsterClass.getName());
			}*/
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

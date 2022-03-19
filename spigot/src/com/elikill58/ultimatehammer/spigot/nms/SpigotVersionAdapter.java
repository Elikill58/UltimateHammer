package com.elikill58.ultimatehammer.spigot.nms;

import static com.elikill58.ultimatehammer.spigot.utils.Utils.VERSION;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.packets.nms.VersionAdapter;
import com.elikill58.ultimatehammer.spigot.utils.PacketUtils;
import com.elikill58.ultimatehammer.universal.Version;

public abstract class SpigotVersionAdapter extends VersionAdapter<Player> {
	
	private Class<?> blockClass, craftWorldClass;
	private Method getNMSBlockMethod, getExpDropMethod;

	public SpigotVersionAdapter(String version) {
		super(version);
		try {
			getNMSBlockMethod = PacketUtils.getObcClass("block.CraftBlock")
					.getDeclaredMethod("getNMS" + (Version.getVersion().isNewerOrEquals(Version.V1_16) ? "" : "Block"));
			getNMSBlockMethod.setAccessible(true);
			blockClass = PacketUtils.getNmsClass("Block", "world.level.block.");
			craftWorldClass = PacketUtils.getObcClass("CraftWorld");
			getExpDropMethod = PacketUtils.getMethodWithName(blockClass, "getExpDrop");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static SpigotVersionAdapter instance;

	public static SpigotVersionAdapter getVersionAdapter() {
		if (instance == null) {
			switch (VERSION) {
			case "v1_7_R4":
				return instance = new Spigot_1_7_R4();
			case "v1_8_R3":
				return instance = new Spigot_1_8_R3();
			case "v1_9_R1":
				return instance = new Spigot_1_9_R1();
			case "v1_9_R2":
				return instance = new Spigot_1_9_R2();
			case "v1_10_R1":
				return instance = new Spigot_1_10_R1();
			case "v1_11_R1":
				return instance = new Spigot_1_11_R1();
			case "v1_12_R1":
				return instance = new Spigot_1_12_R1();
			case "v1_13_R2":
				return instance = new Spigot_1_13_R2();
			case "v1_14_R1":
				return instance = new Spigot_1_14_R1();
			case "v1_15_R1":
				return instance = new Spigot_1_15_R1();
			case "v1_16_R1":
				return instance = new Spigot_1_16_R1();
			case "v1_16_R3":
				return instance = new Spigot_1_16_R3();
			case "v1_17_R1":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.negativity.spigot17.Spigot_1_17_R1").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			case "v1_18_R1":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.negativity.spigot18.Spigot_1_18_R1").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			case "v1_18_R2":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.negativity.spigot18.Spigot_1_18_R2").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			default:
				return instance = new Spigot_UnknowVersion(VERSION);
			}
		}
		return instance;
	}

	public Object getPlayerConnection(Player p) {
		try {
			Object entityPlayer = PacketUtils.getEntityPlayer(p);
			return entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void sendPacket(Player p, Object packet) {
		try {
			Object playerConnection = getPlayerConnection(p);
			playerConnection.getClass().getMethod("sendPacket", PacketUtils.getNmsClass("Packet", "network.protocol.game."))
					.invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getXpToDrop(Block b, int bonusLevel, ItemStack item) {
		try {
			Object nmsBlock = getNMSBlockMethod.invoke(b);
			Object blockData = PacketUtils.getMethodThatReturn(nmsBlock, PacketUtils.getNmsClass("IBlockData", "world.level.block.state."), 0).invoke(nmsBlock);
			Object nmsWorld = craftWorldClass.getMethod("getHandle").invoke(craftWorldClass.cast(((org.bukkit.block.Block) b.getDefault()).getWorld()));
			return (int) getExpDropMethod.invoke(nmsBlock, nmsWorld, blockData, bonusLevel);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}

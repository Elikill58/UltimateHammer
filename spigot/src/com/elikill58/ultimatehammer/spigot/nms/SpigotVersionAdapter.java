package com.elikill58.ultimatehammer.spigot.nms;

import static com.elikill58.ultimatehammer.spigot.utils.Utils.VERSION;

import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.nms.VersionAdapter;

public abstract class SpigotVersionAdapter extends VersionAdapter<Player> {
	
	protected static final String NBT_TAG_KEY = "ultimatehammer-v2";

	public SpigotVersionAdapter(String version) {
		super(version);
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
							.forName("com.elikill58.ultimatehammer.spigot.nms.Spigot_1_17_R1").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			case "v1_18_R1":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.ultimatehammer.spigot.nms.Spigot_1_18_R1").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			case "v1_18_R2":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.ultimatehammer.spigot.nms.Spigot_1_18_R2").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			case "v1_19_R1":
				try {
					return instance = (SpigotVersionAdapter) Class
							.forName("com.elikill58.ultimatehammer.spigot.nms.Spigot_1_19_R1").getConstructor().newInstance();
				} catch (ReflectiveOperationException e) {
					throw new RuntimeException(e);
				}
			default:
				return instance = new Spigot_UnknowVersion(VERSION);
			}
		}
		return instance;
	}
}

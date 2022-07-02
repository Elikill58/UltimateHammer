package com.elikill58.ultimatehammer.utils;

public enum Version {
	
	V1_7("1.7", 7),
	V1_8("1.8", 8),
	V1_9("1.9", 9),
	V1_10("1.10", 10),
	V1_11("1.11", 11),
	V1_12("1.12", 12),
	V1_13("1.13", 13),
	V1_14("1.14", 14),
	V1_15("1.15", 15),
	V1_16("1.16", 16),
	V1_17("1.17", 17),
	V1_18("1.18", 18),
	V1_19("1.19", 19),
	V1_20("1.20", 20),
	HIGHER("higher", 42);

	private final int power;
	private final String name;
	
	Version(String name, int power) {
		this.name = name;
		this.power = power;
	}
	
	public String getName() {
		return name;
	}

	public boolean isNewerThan(Version other) {
		return power > other.getPower();
	}
	
	public boolean isNewerOrEquals(Version other) {
		return power >= other.getPower();
	}

	public int getPower() {
		return power;
	}
	
	public static Version getVersion() {
		return getVersion(PacketUtils.VERSION);
	}
	
	public static Version getVersion(String version) {
		for (Version v : Version.values())
			if (version.toLowerCase().startsWith(v.name().toLowerCase()))
				return v;
		return HIGHER;
	}
}

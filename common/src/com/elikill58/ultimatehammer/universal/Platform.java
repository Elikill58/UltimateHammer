package com.elikill58.ultimatehammer.universal;

public enum Platform {
	
	BUNGEE("bungee", true),
	SPIGOT("spigot", false),
	SPONGE("sponge", false),
	SPONGE8("sponge8", false),
	VELOCITY("velocity", true);
	
	private final String name;
	private final boolean proxy;
	
	Platform(String name, boolean proxy) {
		this.name = name;
		this.proxy = proxy;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isProxy() {
		return proxy;
	}
}

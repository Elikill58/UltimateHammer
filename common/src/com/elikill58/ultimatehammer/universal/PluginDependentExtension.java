package com.elikill58.ultimatehammer.universal;

/**
 * Implemented on extensions that should only be registered if the plugin identified by
 * the provided {@link #getPluginId() plugin id} is enabled on the server.
 *
 * @see Adapter#hasPlugin
 * @see UltimateHammer#loadExtensions
 */
public interface PluginDependentExtension {

	default boolean hasPreRequises() {
		return true;
	}
	
	String getPluginId();
}
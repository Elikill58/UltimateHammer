package com.elikill58.ultimatehammer.api.plugin;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;

public abstract class ExternalPlugin implements UltimateHammerObject {
	
	public abstract String getId();
	
	/**
	 * Check if the plugin is enabled
	 *
	 * @return true if the plugin is enabled
	 */
	public abstract boolean isEnabled();
}

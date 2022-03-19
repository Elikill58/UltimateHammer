package com.elikill58.ultimatehammer.api.commands;

import com.elikill58.ultimatehammer.api.UltimateHammerObject;

public interface CommandSender extends UltimateHammerObject {
	
	void sendMessage(String msg);

	String getName();
}

package com.elikill58.ultimatehammer.api.commands;

import java.util.List;

public interface TabListeners {

	List<String> onTabComplete(CommandSender sender, String[] arg, String prefix);
}

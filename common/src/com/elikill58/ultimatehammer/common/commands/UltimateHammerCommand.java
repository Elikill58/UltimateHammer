package com.elikill58.ultimatehammer.common.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.elikill58.ultimatehammer.api.commands.CommandListeners;
import com.elikill58.ultimatehammer.api.commands.CommandSender;
import com.elikill58.ultimatehammer.api.commands.TabListeners;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.universal.Adapter;

public class UltimateHammerCommand implements CommandListeners, TabListeners {

	@Override
	public boolean onCommand(CommandSender sender, String[] arg, String prefix) {
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] arg, String prefix) {
		List<String> suggestions = new ArrayList<>();
		if (arg.length == 1) {
			// /uh |
			for (Player p : Adapter.getAdapter().getOnlinePlayers()) {
				if (p.getName().toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT)) || prefix.isEmpty()) {
					suggestions.add(p.getName());
				}
			}
		}
		return suggestions;
	}
}

package com.elikill58.ultimatehammer.api.commands;

import java.util.HashMap;
import java.util.Locale;

import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.others.CommandExecutionEvent;
import com.elikill58.ultimatehammer.api.events.others.TabExecutionEvent;
import com.elikill58.ultimatehammer.common.commands.UltimateHammerCommand;

public class CommandManager implements Listeners {

	private final HashMap<String, CommandListeners> commands = new HashMap<>();
	private final HashMap<String, TabListeners> tabs = new HashMap<>();
	
	public CommandManager() {
		UltimateHammerCommand negativity = new UltimateHammerCommand();
		commands.put("negativity", negativity);
		tabs.put("negativity", negativity);
	}
	
	@EventListener
	public void onCommand(CommandExecutionEvent e) {
		CommandListeners cmd = commands.get(e.getCommand().toLowerCase(Locale.ROOT));
		if(cmd != null)
			e.setGoodResult(cmd.onCommand(e.getSender(), e.getArgument(), e.getPrefix()));
	}
	
	@EventListener
	public void onTab(TabExecutionEvent e) {
		TabListeners cmd = tabs.get(e.getCommand().toLowerCase(Locale.ROOT));
		if(cmd != null)
			e.setTabContent(cmd.onTabComplete(e.getSender(), e.getArgument(), e.getPrefix()));
	}
	
}

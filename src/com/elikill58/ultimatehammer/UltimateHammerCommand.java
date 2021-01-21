package com.elikill58.ultimatehammer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UltimateHammerCommand implements CommandExecutor {

	private final UltimateHammer pl;
	
	public UltimateHammerCommand(UltimateHammer pl) {
		this.pl = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
		if(arg.length == 0 || arg[0].equalsIgnoreCase("help")) {
			pl.sendListMessageTo(sender, "help");
		} else if(arg[0].equalsIgnoreCase("reload") && sender.hasPermission("ultimatehammer.reload")) {
			pl.loadData();
			pl.sendMessageTo(sender, "well_reloaded");
		} else {
			String key = arg[0].toLowerCase();
			if(pl.allTools.containsKey(key)) {
				UltimateTool tool = pl.allTools.get(key);
				if(!tool.isEnabled()) {
					pl.sendMessageTo(sender, "not_enabled");
					return false;
				}
				if(sender instanceof Player && !tool.hasPermission((Player) sender)) {
					pl.sendMessageTo(sender, "no_perm");
					return false;
				}
				if(arg.length > 1) {
					Player cible = Bukkit.getPlayer(arg[1]);
					if(cible == null)
						pl.sendMessageTo(sender, "no_player", "%arg%", arg[1]);
					else {
						tool.addItem(cible);
						tool.sendMessage(cible);
						pl.sendMessageTo(sender, "given_other", "%name%", cible.getName());
					}
				} else {
					if(sender instanceof Player) {
						tool.addItem((Player) sender);
						tool.sendMessage((Player) sender);
					} else
						pl.sendMessageTo(sender, "console_cannot");
				}
			} else
				pl.sendMessageTo(sender, "not_found", "%arg%", key);
		}
		return false;
	}

}

package com.elikill58.ultimatehammer.common.commands;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ultimatehammer.api.commands.CommandListeners;
import com.elikill58.ultimatehammer.api.commands.CommandSender;
import com.elikill58.ultimatehammer.api.commands.TabListeners;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Messages;
import com.elikill58.ultimatehammer.universal.UltimateHammer;
import com.elikill58.ultimatehammer.universal.permissions.Perm;

public class UltimateHammerCommand implements CommandListeners, TabListeners {

	@Override
	public boolean onCommand(CommandSender sender, String[] arg, String prefix) {
		if(arg.length == 0 || arg[0].equalsIgnoreCase("help")) {
			if(Perm.hasPerm(sender, "ultimatehammer.reload"))
				Messages.sendMessage(sender, "help.reload");
			Messages.sendMessage(sender, "help.general");
		} else if(arg[0].equalsIgnoreCase("reload") && Perm.hasPerm(sender, "ultimatehammer.reload")) {
			UltimateHammer.loadUltimateHammer();
			Messages.sendMessage(sender, "well_reloaded");
		} else {
			String key = arg[0].toLowerCase();
			if(UltimateTool.getAlltools().containsKey(key)) {
				UltimateTool tool = UltimateTool.getAlltools().get(key);
				if(!tool.isEnabled()) {
					Messages.sendMessage(sender, "not_enabled");
					return false;
				}
				if(sender instanceof Player && !tool.hasPermission((Player) sender)) {
					Messages.sendMessage(sender, "not_permission");
					return false;
				}
				if(arg.length > 1) {
					Player cible = Adapter.getAdapter().getPlayer(arg[1]);
					if(cible == null)
						Messages.sendMessage(sender, "not_player", "%arg%", arg[1]);
					else {
						tool.addItem(cible);
						tool.sendMessage(cible);
						Messages.sendMessage(sender, "given_other", "%name%", cible.getName());
					}
				} else {
					if(sender instanceof Player) {
						tool.addItem((Player) sender);
						tool.sendMessage((Player) sender);
					} else
						Messages.sendMessage(sender, "console_cannot");
				}
			} else
				Messages.sendMessage(sender, "not_found", "%arg%", key);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] arg, String prefix) {
		List<String> list = new ArrayList<>();
		if(arg.length == 1) {
			UltimateTool.getAlltools().forEach((key, tool) -> {
				if((prefix.isEmpty() || key.startsWith(prefix)) && tool.isEnabled() && tool.hasPermission((Player) sender))
					list.add(key);
			});
			if((prefix.isEmpty() || "reload".startsWith(prefix)) && Perm.hasPerm(sender, "ultimatehammer.reload"))
				list.add("reload");
		} else {
			for(Player p : Adapter.getAdapter().getOnlinePlayers())
				if(prefix.isEmpty() || p.getName().toLowerCase().startsWith(prefix))
					list.add(p.getName());
		}
		return list;
	}
}

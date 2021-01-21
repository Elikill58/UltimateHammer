package com.elikill58.ultimatehammer;

import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.elikill58.ultimatehammer.support.factions.FactionsUUIDSupport;
import com.elikill58.ultimatehammer.tools.AxeManager;
import com.elikill58.ultimatehammer.tools.HammerManager;
import com.elikill58.ultimatehammer.tools.HoeManager;
import com.elikill58.ultimatehammer.tools.SwordManager;
import com.elikill58.ultimatehammer.utils.Utils;

public class UltimateHammer extends JavaPlugin {

	public HashMap<String, UltimateTool> allTools = new HashMap<>();
	public static boolean worldGuardSupport = false;
	private static UltimateHammer instance;
	public static UltimateHammer getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		loadData();
		
		getCommand("ultimatehammer").setExecutor(new UltimateHammerCommand(this));
		
		StringJoiner supportedPluginName = new StringJoiner(", ");
		if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
			worldGuardSupport = true;
			supportedPluginName.add("WorldGuard");
		}
		if (Bukkit.getPluginManager().getPlugin("Factions") != null && Utils.foundClass("com.massivecraft.factions.perms.PermissibleAction")) {
			WorldRegionBypass.BUILD_CHECKER.add(new FactionsUUIDSupport());
			supportedPluginName.add("Factions");
		}
		
		if (supportedPluginName.length() > 0) {
			getLogger().info("Loaded support for " + supportedPluginName.toString() + ".");
		}
		
		WorldRegionBypass.load(this);
	}
	
	public void loadData() {
		saveDefaultConfig();
		reloadConfig();
		allTools.forEach((key, tool) -> tool.unregister());
		allTools.clear();

		allTools.put("hammer", new HammerManager(this));
		allTools.put("axe", new AxeManager(this));
		allTools.put("hoe", new HoeManager(this));
		allTools.put("sword", new SwordManager(this));
	}
	
	public void sendListMessageTo(CommandSender p, String msg, String... placeholders) {
		List<String> list = getConfig().getStringList("messages." + msg);
		if(list.isEmpty())
			p.sendMessage(msg);
		else
			for(String s : list)
				p.sendMessage(Utils.applyPlaceHolders(s, placeholders));
	}
	
	public void sendMessageTo(CommandSender p, String msg, String... placeholders) {
		p.sendMessage(Utils.applyPlaceHolders(getConfig().getString("messages." + msg, msg), placeholders));
	}
}

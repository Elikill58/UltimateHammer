package com.elikill58.ultimatehammer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.elikill58.ultimatehammer.nms.NMS;
import com.elikill58.ultimatehammer.utils.Utils;

public abstract class UltimateTool implements Listener {

	protected final UltimateHammer pl;
	protected final String key;
	protected final String message, permission;
	private boolean isEnabled = false;
	protected ItemStack item, defaultItem;
	private final ConfigurationSection section;
	
	public UltimateTool(UltimateHammer pl, String key) {
		this.pl = pl;
		this.key = key;
		this.message = Utils.coloredMessage(pl.getConfig().getString(key + ".message", ""));
		this.permission = pl.getConfig().getString(key + ".permission", "");
		pl.getServer().getPluginManager().registerEvents(this, pl);
		section = pl.getConfig().getConfigurationSection(key);
		if(section == null) {
			pl.getLogger().severe("Cannot find '" + key + "' section in config !");
		} else {
			isEnabled = section.getBoolean("enable");
			if(isEnabled) {
				item = NMS.getNMS().addNbtTag(Utils.getItem(section), key);
				defaultItem = item.clone();
				pl.getLogger().info("Enabled " + key + " !");
			}
		}
	}
	
	public String getKey() {
		return key;
	}

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public UltimateHammer getPlugin() {
		return pl;
	}
	
	public ConfigurationSection getConfigSection() {
		return section;
	}
	
	public boolean isItem(ItemStack inHand) {
		return inHand != null && NMS.getNMS().hasNbtTag(inHand, key);
	}
	
	public void sendMessage(Player p) {
		if(!message.isEmpty())
			p.sendMessage(message);
	}
	
	public void addItem(Player p) {
		p.getInventory().addItem(defaultItem.clone());
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public boolean hasPermission(Player p) {
		return permission.isEmpty() || p.hasPermission(permission);
	}
	
	public void unregister() {
		HandlerList.unregisterAll(this);
	}
}

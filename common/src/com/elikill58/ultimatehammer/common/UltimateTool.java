package com.elikill58.ultimatehammer.common;

import java.util.List;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.utils.Utils;
import com.elikill58.ultimatehammer.api.yaml.Configuration;

public class UltimateTool {

	private final String key;
	private final Configuration section;
	private List<String> types;
	private String message, permission;
	private boolean isEnabled = false;
	private ItemStack item, defaultItem;
	// hammer config
	private final List<Material> blacklistHammer;
	// hoe config
	private final int hoeSize;
	
	public UltimateTool(Configuration section, String key) {
		this.key = key;
		this.section = section;
		this.isEnabled = section.getBoolean("enable");
		this.message = Utils.coloredMessage(section.getString("message", ""));
		this.permission = section.getString("permission");
		this.types = section.getStringList("type");
		
		blacklistHammer = section.getStringList("hammer.blacklist").stream().map(s -> ItemRegistrar.getInstance().get(s)).collect(Collectors.toList());
		blacklistHammer.add(Materials.BEDROCK);
		
		this.hoeSize = section.getInt("hoe.size", 3);
		
		if(isEnabled) {
			this.item = ItemStack.getItem(section);
			this.defaultItem = item.clone();
		}
	}
	
	public String getKey() {
		return key;
	}

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public Configuration getConfigSection() {
		return section;
	}
	
	public List<Material> getBlacklistHammer() {
		return blacklistHammer;
	}
	
	public boolean isItem(ItemStack inHand) {
		if(inHand == null || inHand.getType().getId().contains("AIR"))
			return false;
		return item.clone().isSimilarExceptDamage(inHand);
	}

	public void used(Player p, String string, Block b) {}
	
	public int getHoeSize() {
		return hoeSize;
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
	
	public List<String> getTypes() {
		return types;
	}
	
	public boolean hasPermission(Player p) {
		return permission.isEmpty() || p.hasPermission(permission);
	}
}

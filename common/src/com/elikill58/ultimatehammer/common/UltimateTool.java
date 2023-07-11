package com.elikill58.ultimatehammer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.nms.VersionAdapter;
import com.elikill58.ultimatehammer.api.utils.Utils;
import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.UltimateHammer;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;
import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class UltimateTool {

	public static final String NBT_TAG_KEY = "ultimatehammer-v2";
	
	private static final List<UsedActionManager> USED_ACTIONS = new ArrayList<>();
	private static final HashMap<String, UltimateTool> ALL_TOOLS = new HashMap<>();
	public static HashMap<String, UltimateTool> getAlltools() {
		return ALL_TOOLS;
	}
	public static Optional<UltimateTool> getTool(ItemStack item) {
		for(UltimateTool tool : new ArrayList<>(ALL_TOOLS.values())) {
			if(tool.isItem(item)) {
				return Optional.of(tool);
			}
		}
		return Optional.empty();
	}
	
	public static void init() {
		USED_ACTIONS.clear();
		Adapter adapter = Adapter.getAdapter();
		UltimateHammer.loadExtensions(UsedActionManagerProvider.class, provider -> {
			UsedActionManager usedActions = provider.create(adapter);
			if (usedActions != null) {
				USED_ACTIONS.add(usedActions);
				return true;
			}
			return false;
		});
		
		ALL_TOOLS.clear();
		int enabled = 0;
		Configuration itemConfig = adapter.getConfig().getSection("items");
		for(String key : itemConfig.getKeys()) {
			UltimateTool tool = new UltimateTool(itemConfig.getSection(key), key);
			if(tool.isEnabled())
				enabled++;
			ALL_TOOLS.put(key, tool);
		}
		adapter.getLogger().info("Loaded " + ALL_TOOLS.size() + " tools (" + enabled + " enabled).");
	}
	
	private final String key;
	private final Configuration section;
	private List<String> types;
	private String message, permission;
	private boolean isEnabled = false, renamable = true, craftable = false;
	private ItemStack item, defaultItem;
	// hammer config
	private final List<Material> blacklistHammer;
	// hoe config
	private final int hoeSize, hammerSize, hammerLayerSize, useAmount;
	
	public UltimateTool(Configuration section, String key) {
		this.key = key;
		this.section = section;
		this.isEnabled = section.getBoolean("enable", false);
		this.renamable = section.getBoolean("renamable", true);
		this.craftable = section.getBoolean("craftable", false);
		this.message = Utils.coloredMessage(section.getString("message", ""));
		this.permission = section.getString("permission");
		this.types = section.getStringList("type");
		this.useAmount = section.getInt("uses", -1);
		
		blacklistHammer = section.getStringList("hammer.blacklist").stream().map(s -> ItemRegistrar.getInstance().get(s)).collect(Collectors.toList());
		blacklistHammer.add(Materials.BEDROCK);

		this.hoeSize = section.getInt("hoe.size", 3);
		this.hammerSize = section.getInt("hammer.size", 3);
		this.hammerLayerSize = section.getInt("hammer.layer", 1);
		
		if(isEnabled) {
			VersionAdapter<?> va = Adapter.getAdapter().getVersionAdapter();
			ItemStack basicItem = va.setNbtTag(ItemStack.getItem(section), NBT_TAG_KEY, key);
			if(!isRenamable())
				basicItem = va.setNbtTag(basicItem, "RepairCost", 100); // 100 more than 40 -> "too expensive"
			this.item = basicItem;
			this.defaultItem = basicItem.clone();
		}
	}
	
	public String getKey() {
		return key;
	}

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public ItemStack parseLore(ItemStack item) {
		if(item.getLore().isEmpty())
			return item;
		int use = item.getNbtTagInt(UltimateTool.NBT_TAG_KEY + "-uses", 0);
		List<String> lore = new ArrayList<>(item.getLore());
		for(int i = 0; i < lore.size(); i++) {
			lore.set(i, UniversalUtils.replacePlaceholders(lore.get(i), "%uses%", use));
		}
		item.setLore(lore);
		return item;
	}
	
	/**
	 * Check if can rename this item
	 * 
	 * @return true if can rename
	 */
	public boolean isRenamable() {
		return renamable;
	}
	
	public boolean isCraftable() {
		return craftable;
	}
	
	public Configuration getConfigSection() {
		return section;
	}
	
	public int getUseAmount() {
		return useAmount;
	}
	
	public List<Material> getBlacklistHammer() {
		return blacklistHammer;
	}
	
	public boolean isItem(ItemStack inHand) {
		if(inHand == null || inHand.getType().getId().contains("AIR"))
			return false;
		return inHand.hasNbtTag(NBT_TAG_KEY, key);
	}

	public boolean usedBreak(Player p, Block b) {
		for(UsedActionManager u : USED_ACTIONS) {
			if(u.usedBreak(this, p, p.getItemInHand(), b))
				return true;
		}
		return false;
	}
	
	public int getHoeSize() {
		return hoeSize;
	}
	
	public int getHammerSize() {
		return hammerSize;
	}
	
	public int getHammerLayerSize() {
		return hammerLayerSize;
	}
	
	public void sendMessage(Player p) {
		if(!message.isEmpty())
			p.sendMessage(message);
	}
	
	public void addItem(Player p) {
		p.getInventory().addItem(parseLore(defaultItem.clone()));
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

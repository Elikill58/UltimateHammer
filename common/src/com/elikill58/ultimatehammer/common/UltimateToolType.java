package com.elikill58.ultimatehammer.common;

import java.util.List;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.UltimateHammer;

public abstract class UltimateToolType {
	
	private final String key;
	
	public UltimateToolType(String key) {
		this.key = key;
		Adapter.getAdapter().getLogger().info("Loaded tool type " + key + ".");
	}
	
	public String getKey() {
		return key;
	}
	
	public List<UltimateTool> getTool(Player p) {
		return UltimateHammer.getAlltools().values().stream().filter(UltimateTool::isEnabled).filter((u) -> u.getTypes().contains(key)).collect(Collectors.toList());
	}
	
	public List<UltimateTool> getToolForHand(Player p) {
		return UltimateHammer.getAlltools().values().stream().filter(UltimateTool::isEnabled).filter((u) -> u.getTypes().contains(key) && u.isItem(p.getItemInHand())).collect(Collectors.toList());
	}
}

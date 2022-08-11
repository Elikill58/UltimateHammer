package com.elikill58.ultimatehammer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.universal.Adapter;

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
		return UltimateTool.getAlltools().values().stream().filter(UltimateTool::isEnabled).filter((u) -> u.getTypes().contains(key)).collect(Collectors.toList());
	}
	
	public Optional<UltimateTool> getToolForHand(Player p) {
		for(UltimateTool allTool : new ArrayList<>(UltimateTool.getAlltools().values()))
			if(allTool.getTypes().contains(key) && allTool.isItem(p.getItemInHand()))
				return Optional.of(allTool);
		return Optional.empty();
	}
}

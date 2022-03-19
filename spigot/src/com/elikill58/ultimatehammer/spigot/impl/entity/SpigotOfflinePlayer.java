package com.elikill58.ultimatehammer.spigot.impl.entity;

import java.util.UUID;

import com.elikill58.ultimatehammer.api.entity.AbstractEntity;
import com.elikill58.ultimatehammer.api.entity.BoundingBox;
import com.elikill58.ultimatehammer.api.entity.OfflinePlayer;

public class SpigotOfflinePlayer extends AbstractEntity implements OfflinePlayer {

	private final org.bukkit.OfflinePlayer op;
	
	public SpigotOfflinePlayer(org.bukkit.OfflinePlayer op) {
		this.op = op;
	}

	@Override
	public boolean isOp() {
		return op.isOp();
	}

	@Override
	public Object getDefault() {
		return op;
	}

	@Override
	public String getName() {
		return op.getName();
	}

	@Override
	public boolean isOnline() {
		return op.isOnline();
	}

	@Override
	public UUID getUniqueId() {
		return op.getUniqueId();
	}
	
	@Override
	public String getEntityId() {
		return op.getUniqueId().toString();
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return null;
	}
}

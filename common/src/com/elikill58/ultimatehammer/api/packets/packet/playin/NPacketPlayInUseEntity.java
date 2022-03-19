package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.location.Vector;
import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInUseEntity implements NPacketPlayIn {

	public int entityId;
	/**
	 * WARN: this value seems to be not present in 1.17+
	 */
	public Vector vector;
	public EnumEntityUseAction action;

	public NPacketPlayInUseEntity() {

	}

	public NPacketPlayInUseEntity(int entityId, Vector vector, EnumEntityUseAction action) {
		this.entityId = entityId;
		this.vector = vector;
		this.action = action;
	}

	public static enum EnumEntityUseAction {
		INTERACT, ATTACK, INTERACT_AT;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Client.USE_ENTITY;
	}
}

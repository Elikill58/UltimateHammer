package com.elikill58.ultimatehammer.api.packets.packet.playout;

import com.elikill58.ultimatehammer.api.location.Vector;
import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayOut;

public class NPacketPlayOutEntityVelocity implements NPacketPlayOut {

	public int entityId;
	public Vector vec;

	public NPacketPlayOutEntityVelocity() {
		
	}
	
	public NPacketPlayOutEntityVelocity(int entityId, int x, int y, int z) {
		this.entityId = entityId;
		this.vec = new Vector(x, y, z);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Server.ENTITY_VELOCITY;
	}
}

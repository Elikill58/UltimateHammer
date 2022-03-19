package com.elikill58.ultimatehammer.api.packets.packet.playout;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayOut;

public class NPacketPlayOutKeepAlive implements NPacketPlayOut {

	public long time;
	
	public NPacketPlayOutKeepAlive() {
		
	}

	public NPacketPlayOutKeepAlive(long time) {
		this.time = time;
	}
	
	@Override
	public PacketType getPacketType() {
		return PacketType.Server.KEEP_ALIVE;
	}
}

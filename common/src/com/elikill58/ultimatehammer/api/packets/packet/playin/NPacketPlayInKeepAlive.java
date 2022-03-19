package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInKeepAlive implements NPacketPlayIn {

	public long time;
	
	public NPacketPlayInKeepAlive() {
		
	}

	public NPacketPlayInKeepAlive(long time) {
		this.time = time;
	}
	
	@Override
	public PacketType getPacketType() {
		return PacketType.Client.KEEP_ALIVE;
	}
}

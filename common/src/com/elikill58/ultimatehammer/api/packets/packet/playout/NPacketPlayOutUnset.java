package com.elikill58.ultimatehammer.api.packets.packet.playout;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayOut;

public class NPacketPlayOutUnset implements NPacketPlayOut {

	public final String packetName;
	
	public NPacketPlayOutUnset(String packetName) {
		this.packetName = packetName;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Server.UNSET;
	}
}

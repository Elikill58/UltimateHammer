package com.elikill58.ultimatehammer.api.packets.packet;

import com.elikill58.ultimatehammer.api.packets.PacketType;

public class NPacketUnknown implements NPacket {


	@Override
	public PacketType getPacketType() {
		return PacketType.Client.UNSET;
	}
}

package com.elikill58.ultimatehammer.api.packets.packet.handshake;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.PacketType.Handshake;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketHandshake;

public class NPacketHandshakeUnset implements NPacketHandshake {

	@Override
	public PacketType getPacketType() {
		return Handshake.UNSET;
	}

}

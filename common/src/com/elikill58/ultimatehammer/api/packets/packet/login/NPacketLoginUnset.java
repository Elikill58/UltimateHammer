package com.elikill58.ultimatehammer.api.packets.packet.login;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketLogin;

public class NPacketLoginUnset implements NPacketLogin {

	
	public NPacketLoginUnset() {
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Login.UNSET;
	}
}

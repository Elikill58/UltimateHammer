package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInChat implements NPacketPlayIn {

	public String message;
	
	public NPacketPlayInChat() {
		
	}


	public NPacketPlayInChat(String message) {
		this.message = message;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Client.CHAT;
	}
}

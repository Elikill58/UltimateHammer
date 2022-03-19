package com.elikill58.ultimatehammer.api.packets.packet.playout;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayOut;

/**
 * For 1.16 and lower, this is the "PacketPlayOutTransaction" packet.
 * 
 * @author Elikill58
 */
public class NPacketPlayOutPing implements NPacketPlayOut {

	public int id;

	public NPacketPlayOutPing() {
	}

	public NPacketPlayOutPing(int id) {
		this.id = id;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Server.PING;
	}

}

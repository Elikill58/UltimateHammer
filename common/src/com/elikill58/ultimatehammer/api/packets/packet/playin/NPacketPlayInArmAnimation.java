package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInArmAnimation implements NPacketPlayIn {

	public long timestamp;
	
	public NPacketPlayInArmAnimation() {
		
	}
	
	public NPacketPlayInArmAnimation(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Client.ARM_ANIMATION;
	}
}

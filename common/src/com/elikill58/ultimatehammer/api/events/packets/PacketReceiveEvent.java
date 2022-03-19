package com.elikill58.ultimatehammer.api.events.packets;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.CancellableEvent;
import com.elikill58.ultimatehammer.api.packets.AbstractPacket;

public class PacketReceiveEvent extends PacketEvent implements CancellableEvent {

	public PacketReceiveEvent(PacketSourceType source, AbstractPacket packet, Player p) {
		super(source, packet, p);
	}

	@Override
	public boolean isCancelled() {
		return getPacket().isCancelled();
	}

	@Override
	public void setCancelled(boolean cancel) {
		getPacket().setCancelled(cancel);
	}
}

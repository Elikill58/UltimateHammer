package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInFlying implements NPacketPlayIn {

	public double x, y, z;
	public float yaw, pitch;
	public boolean hasPos = false, hasLook = false, isGround = false;
	
	public NPacketPlayInFlying() {
		
	}

	public NPacketPlayInFlying(double x, double y, double z, float yaw, float pitch, boolean isGround, boolean hasPos, boolean hasLook) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.isGround = isGround;
		this.hasPos = hasPos;
		this.hasLook = hasLook;
	}
	
	/**
	 * Warn: THIS CAN RETURN NULL.<br>
	 * If return a {@link Location} object but only according to {@link #hasPos} and {@link #hasLook} values.<br>
	 * If only has look, if will only return null because others values will be totally bugged
	 * 
	 * @param w the world on the location
	 * @return a location or null
	 */
	public Location getLocation(World w) {
		if(hasPos && hasLook) // if fully move
			return new Location(w, x, y, z, yaw, pitch);
		else if(hasPos) // if just walk
			return new Location(w, x, y, z);
		else
			return null; // no real location
	}
	
	@Override
	public PacketType getPacketType() {
		return PacketType.Client.FLYING;
	}
}

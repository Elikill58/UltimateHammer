package com.elikill58.ultimatehammer.api.packets.packet.playin;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.packets.PacketType;
import com.elikill58.ultimatehammer.api.packets.packet.NPacketPlayIn;

public class NPacketPlayInBlockDig implements NPacketPlayIn {

	public int x, y, z;
	public DigFace face;
	public DigAction action;
	
	public NPacketPlayInBlockDig() {
		
	}

	public NPacketPlayInBlockDig(int x, int y, int z, DigAction action, DigFace face) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
		this.action = action;
	}
	
	public Block getBlock(World w) {
		return w.getBlockAt(x, y, z);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.Client.BLOCK_DIG;
	}
	
	public static enum DigAction {

		START_DIGGING(0),
		CANCEL_DIGGING(1),
		FINISHED_DIGGING(2),
		
		/**
		 * Drop the entire of the item stack
		 */
		DROP_ITEM_STACK(3),
		
		/**
		 * Drop one item
		 */
		DROP_ITEM(4),
		
		/**
		 * The finished action can correspond to:<br>
		 * - Shoot arrow<br>
		 * - Finish eating<br>
		 * - Use bucket<br>
		 * - ...
		 */
		FINISH_ACTION(5),
		
		/**
		 * Action when item is swipped between two item
		 */
		SWAP_ITEM(6);
		
		private final int id;
		
		private DigAction(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public static DigAction getById(int id) {
			for(DigAction da : values())
				if(da.getId() == id)
					return da;
			return null;
		}
	}

	public static enum DigFace {

		BOTTOM(0),
		TOP(1),
		NORTH(2),
		SOUTH(3),
		WEST(4),
		EAST(5);
		
		private final int id;
		
		private DigFace(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public static DigFace getById(int id) {
			for(DigFace da : values())
				if(da.getId() == id)
					return da;
			return null;
		}
	}
}

package com.elikill58.ultimatehammer.spigot.impl.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import com.elikill58.ultimatehammer.api.block.BlockData;
import com.elikill58.ultimatehammer.api.block.BlockFace;

@SuppressWarnings("deprecation")
public class SpigotBlockDataOld extends BlockData {

	private final Block b;
	
	public SpigotBlockDataOld(Block b) {
		this.b = b;
	}

	@Override
	public boolean isLeavesPersistent() {
		return false;
	}

	@Override
	public int getLeavesDistance() {
		return b.getData();
	}

	@Override
	public int getHumidity() {
		return b.getData();
	}

	@Override
	public int getMaximumHumidity() {
		return 7;
	}

	@Override
	public int getAge() {
		MaterialData md = b.getType().getNewData(b.getData());
		if(md instanceof CocoaPlant) {
			CocoaPlant cp = (CocoaPlant) md;
			switch (cp.getSize()) {
			case LARGE:
				return 3;
			case MEDIUM:
				return 2;
			case SMALL:
				return 1;
			default:
				return -1;
			}
		}
		return b.getData();
	}

	@Override
	public int getMaximumAge() {
		return -1;
	}
	
	@Override
	public BlockFace getFacing() {
		MaterialData md = b.getType().getNewData(b.getData());
		return md instanceof Directional ? BlockFace.valueOf(((Directional) md).getFacing().name()) : null;
	}
	
	@Override
	public void setFacing(BlockFace face) {
		MaterialData md = b.getType().getNewData(b.getData());
		if(md instanceof Directional) {
			((Directional) md).setFacingDirection(org.bukkit.block.BlockFace.valueOf(face.name()));
			BlockState bs = b.getState();
			bs.setData(md);
			bs.update();
		}
	}
}

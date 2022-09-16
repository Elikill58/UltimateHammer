package com.elikill58.ultimatehammer.common.tools.hoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockFace;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;

public class Plantable {

	private final Material inventoryItem, nextItem;
	private final byte neededDataToGet;
	
	public Plantable(Material inventoryItem, Material nextItem) {
		this(inventoryItem, nextItem, (byte) -1);
	}
	
	public Plantable(Material inventoryItem, Material nextItem, byte neededDataToGet) {
		this.inventoryItem = inventoryItem;
		this.nextItem = nextItem;
		this.neededDataToGet = neededDataToGet;
	}

	public Material getInventoryItem() {
		return inventoryItem;
	}

	public Material getNextItem() {
		return nextItem;
	}
	
	public byte getNeededDataToGet() {
		return neededDataToGet;
	}
	
	@Override
	public String toString() {
		return inventoryItem.getId() + " > " + nextItem.getId() + ": " + neededDataToGet;
	}
	
	public static enum PlantableType {
		WOOD(true, Materials.JUNGLE_LOG),
		BASIC(false, Materials.DIRT, Materials.GRASS, Materials.SOIL),
		NETHER(false, Materials.SOUL_SAND);
		
		private final boolean vertical;
		private final List<Material> material;
		private final List<Plantable> plantage = new ArrayList<>();
		
		private PlantableType(boolean vertical, Material... mat) {
			this.vertical = vertical;
			this.material = new LinkedList<>(Arrays.asList(mat));
		}

		public List<Material> getMaterial() {
			return material;
		}

		public List<Plantable> getPlantage() {
			return plantage;
		}
		
		public boolean isVertical() {
			return vertical;
		}
		
		public void addPlantage(Plantable pl) {
			this.plantage.add(pl);
		}
		
		public Plantable getPlantageHasInventoryItem(Material m) {
			return plantage.stream().filter((pl) -> pl.getInventoryItem().equals(m)).findFirst().orElse(null);
		}
		
		public static PlantableType getPlantageTypeForBaseBlock(Block baseBlock) {
			BlockFace facing = baseBlock.getBlockData().getFacing();
			Block base = baseBlock.getRelative(facing == null ? BlockFace.DOWN : facing);
			return getPlantageType(base.getType());
		}
		
		public static PlantableType getPlantageType(Material m) {
			for(PlantableType type : values())
				if(type.getMaterial().contains(m))
					return type;
			return null;
		}

		
		public static Plantable getPlantage(Material m) {
			for(PlantableType type : Arrays.asList(values()))
				for(Plantable pl : type.getPlantage())
					if(pl.getNextItem().equals(m))
						return pl;
			return null;
		}
	}
}

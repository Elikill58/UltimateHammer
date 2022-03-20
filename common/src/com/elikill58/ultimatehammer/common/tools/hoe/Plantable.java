package com.elikill58.ultimatehammer.common.tools.hoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	
	public static enum PlantableType {
		BASIC(Materials.DIRT, Materials.GRASS, Materials.SOIL),
		NETHER(Materials.SOUL_SAND);
		
		private final List<Material> material;
		private final List<Plantable> plantage = new ArrayList<>();
		
		private PlantableType(Material... mat) {
			material = new LinkedList<>(Arrays.asList(mat));
		}

		public List<Material> getMaterial() {
			return material;
		}

		public List<Plantable> getPlantage() {
			return plantage;
		}
		
		public void addPlantage(Plantable pl) {
			this.plantage.add(pl);
		}
		
		public Plantable getPlantageHasInventoryItem(Material m) {
			return plantage.stream().filter((pl) -> pl.getInventoryItem().equals(m)).findFirst().orElse(null);
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

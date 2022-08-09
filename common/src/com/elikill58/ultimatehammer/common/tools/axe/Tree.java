package com.elikill58.ultimatehammer.common.tools.axe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.universal.Version;

public enum Tree {
	
	OAK(Version.V1_7, "OAK"),
	BIRCH(Version.V1_7, "BIRCH"),
	SPRUCE(Version.V1_7, "SPRUCE"),
	JUNGLE(Version.V1_7, "JUNGLE"),
	DARK_OAK(Version.V1_7, "DARK_OAK"),
	ACACIA(Version.V1_7, "ACACIA"),
	CRIMSON(Version.V1_16, Arrays.asList("CRIMSON_STEM", "CRIMSON_HYPHAE"), Arrays.asList("NETHER_WART_BLOCK", "SHROOMLIGHT"), true),
	WARPED(Version.V1_16, Arrays.asList("WARPED_STEM", "WARPED_HYPHAE"), Arrays.asList("NETHER_WART_BLOCK", "SHROOMLIGHT"), true),
	AZALEA(Version.V1_17, Arrays.asList("OAK_LOG", "OAK_WOOD"), Arrays.asList("AZALEA_LEAVES", "FLOWERING_AZALEA_LEAVES")),
	MANGROVE(Version.V1_19, Arrays.asList("MANGROVE_LOG", "MANGROVE_WOOD"), Arrays.asList("MANGROVE_ROOTS", "MANGROVE_LEAVES"));

	public final Version minVersion;
	public final List<Material> trunk, leaves;
	public final boolean diagonalLeave;

	Tree(Version minVersion, List<String> trunk, List<String> leaves) {
		this(minVersion, trunk, leaves, false);
	}

	Tree(Version minVersion, List<String> trunk, List<String> leaves, boolean diagonalLeave) {
		this.minVersion = minVersion;
		if(Version.getVersion().isNewerOrEquals(minVersion)) {
			ItemRegistrar ir = ItemRegistrar.getInstance();
			this.trunk = trunk.stream().map(ir::get).collect(Collectors.toList());
			this.leaves = leaves.stream().map(ir::get).collect(Collectors.toList());
		} else {
			this.trunk = new ArrayList<>();
			this.leaves = new ArrayList<>();
		}
		this.diagonalLeave = diagonalLeave;
	}

	Tree(Version minVersion, String name) {
		this.minVersion = minVersion;
		if(Version.getVersion().isNewerOrEquals(minVersion)) {
			ItemRegistrar ir = ItemRegistrar.getInstance();
			this.trunk = Arrays.asList(ir.get(name + "_LOG"), ir.get(name + "_WOOD"));
			this.leaves = Arrays.asList(ir.get(name + "_LEAVES"));
		} else {
			this.trunk = new ArrayList<>();
			this.leaves = new ArrayList<>();
		}
		this.diagonalLeave = false;
	}
	
	public Version getVersion() {
		return minVersion;
	}
	
	public List<Material> getLeaves() {
		return leaves;
	}
	
	public List<Material> getTrunk() {
		return trunk;
	}
	
	public boolean isDiagonalLeave() {
		return diagonalLeave;
	}
}
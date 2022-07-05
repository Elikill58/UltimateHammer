package com.elikill58.ultimatehammer.tools.axe;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.block.Block;

public class DetectedTree {

	public final Tree tree;
	public final HashMap<Integer, ArrayList<Block>> trunk;

	public DetectedTree(Tree tree, HashMap<Integer, ArrayList<Block>> trunk,
			HashMap<Integer, ArrayList<Block>> leaves) {
		this.tree = tree;
		this.trunk = trunk;
	}

	public Tree getTree() {
		return tree;
	}
	
	public HashMap<Integer, ArrayList<Block>> getTrunk() {
		return trunk;
	}
}
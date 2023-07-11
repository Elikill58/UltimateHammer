package com.elikill58.ultimatehammer.common.tools.axe;

import java.util.ArrayList;
import java.util.HashMap;

import com.elikill58.ultimatehammer.api.block.Block;

public class DetectedTree {

	public final Tree tree;
	public final HashMap<Integer, ArrayList<Block>> trunk;
	public int damageApplied = 0;

	public DetectedTree(Tree tree, HashMap<Integer, ArrayList<Block>> trunk, HashMap<Integer, ArrayList<Block>> leaves, int damageApplied) {
		this.tree = tree;
		this.trunk = trunk;
		this.damageApplied = damageApplied;
	}

	public Tree getTree() {
		return tree;
	}

	public HashMap<Integer, ArrayList<Block>> getTrunk() {
		return trunk;
	}
	
	public int getDamageApplied() {
		return damageApplied;
	}
}
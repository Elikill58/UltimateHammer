package com.elikill58.ultimatehammer.spigot.nms;

import java.lang.reflect.Method;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.item.ItemStack;

public class Spigot_1_8_R3 extends SpigotVersionAdapter {

	public Spigot_1_8_R3() {
		super("v1_8_R3");
	}
	
	@Override
	public int getXpToDrop(Block b, int bonusLevel, ItemStack item) {
		try {
			CraftBlock cb = (CraftBlock) b.getDefault();
			Method nmsMethod = cb.getClass().getDeclaredMethod("getNMSBlock");
			nmsMethod.setAccessible(true);
			net.minecraft.server.v1_8_R3.Block nmsBlock = (net.minecraft.server.v1_8_R3.Block) nmsMethod.invoke(cb);
			return nmsBlock.getExpDrop(((CraftWorld) cb.getWorld()).getHandle(), nmsBlock.getBlockData(), bonusLevel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}

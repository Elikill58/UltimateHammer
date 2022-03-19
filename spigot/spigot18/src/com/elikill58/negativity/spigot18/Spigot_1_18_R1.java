package com.elikill58.negativity.spigot18;

import org.bukkit.craftbukkit.v1_18_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.spigot.nms.SpigotVersionAdapter;
import com.elikill58.ultimatehammer.spigot.utils.PacketUtils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class Spigot_1_18_R1 extends SpigotVersionAdapter {

	public Spigot_1_18_R1() {
		super("v1_18_R1");
	}
	
	@Override
	public int getXpToDrop(Block b, int bonusLevel, ItemStack item) {
		CraftBlock obcBlock = (CraftBlock) b.getDefault();
		BlockState blockData = obcBlock.getNMS();
		return blockData.getBlock().getExpDrop(blockData, obcBlock.getCraftWorld().getHandle(), obcBlock.getPosition(), CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item.getDefault()));
	}
	
	@Override
	public void sendPacket(Player p, Object packet) {
		((ServerPlayer) PacketUtils.getEntityPlayer(p)).connection.send((Packet<?>) packet);
	}
}

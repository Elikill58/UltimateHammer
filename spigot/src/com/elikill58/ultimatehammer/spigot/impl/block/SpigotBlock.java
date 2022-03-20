package com.elikill58.ultimatehammer.spigot.impl.block;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.data.Waterlogged;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockFace;
import com.elikill58.ultimatehammer.api.item.ItemRegistrar;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.spigot.impl.item.SpigotItemStack;
import com.elikill58.ultimatehammer.spigot.impl.location.SpigotLocation;
import com.elikill58.ultimatehammer.spigot.impl.location.SpigotWorld;
import com.elikill58.ultimatehammer.universal.Version;

public class SpigotBlock extends Block {

	private final org.bukkit.block.Block block;

	public SpigotBlock(org.bukkit.block.Block block) {
		this.block = block;
	}

	@Override
	public Material getType() {
		return ItemRegistrar.getInstance().get(block.getType().name());
	}

	@Override
	public int getX() {
		return block.getX();
	}

	@Override
	public int getY() {
		return block.getY();
	}

	@Override
	public int getZ() {
		return block.getZ();
	}

	@Override
	public Block getRelative(BlockFace blockFace) {
		return new SpigotBlock(block.getRelative(org.bukkit.block.BlockFace.valueOf(blockFace.name())));
	}

	@Override
	public Location getLocation() {
		return SpigotLocation.toCommon(block.getLocation());
	}

	@Override
	public boolean isLiquid() {
		return block.isLiquid();
	}

	@Override
	public void setType(Material type) {
		block.setType((org.bukkit.Material) type.getDefault());
	}
	
	@Override
	public boolean isWaterLogged() {
		return Version.getVersion().isNewerOrEquals(Version.V1_13) && (block instanceof Waterlogged) && ((Waterlogged) block).isWaterlogged();
	}
	
	@Override
	public World getWorld() {
		return new SpigotWorld(block.getWorld());
	}

	@Override
	public Object getDefault() {
		return block;
	}

	@Override
	public void breakNaturally(ItemStack item) {
		block.breakNaturally((org.bukkit.inventory.ItemStack) item.getDefault());
	}

	@Override
	public List<ItemStack> getDrops(ItemStack item) {
		return block.getDrops((org.bukkit.inventory.ItemStack) item.getDefault()).stream().map(SpigotItemStack::new).collect(Collectors.toList());
	}

	@SuppressWarnings("deprecation")
	@Override
	public byte getData() {
		return block.getData();
	}
}

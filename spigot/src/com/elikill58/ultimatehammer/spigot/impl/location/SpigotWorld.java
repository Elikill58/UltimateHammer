package com.elikill58.ultimatehammer.spigot.impl.location;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.location.Difficulty;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.spigot.impl.block.SpigotBlock;
import com.elikill58.ultimatehammer.universal.Version;

public class SpigotWorld extends World {

	private final org.bukkit.World w;
	
	public SpigotWorld(org.bukkit.World w) {
		this.w = w;
	}

	@Override
	public String getName() {
		return w.getName();
	}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		return new SpigotBlock(w.getBlockAt(x, y, z));
	}

	@Override
	public Block getBlockAt(Location loc) {
		return new SpigotBlock(w.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
	}
	
	@Override
	public Difficulty getDifficulty() {
		return Difficulty.valueOf(w.getDifficulty().name());
	}
	
	@Override
	public int getMaxHeight() {
		return w.getMaxHeight();
	}
	
	@Override
	public int getMinHeight() {
		return Version.getVersion().isNewerOrEquals(Version.V1_18) ? -64 : 0;
	}

	@Override
	public Object getDefault() {
		return w;
	}

	@Override
	public void dropItemNaturally(Location location, ItemStack item) {
		w.dropItemNaturally(SpigotLocation.fromCommon(location), (org.bukkit.inventory.ItemStack) item.getDefault());
	}

	@Override
	public void spawnExperienceOrb(Location loc, int xp) {
		((ExperienceOrb) w.spawnEntity(SpigotLocation.fromCommon(loc), EntityType.EXPERIENCE_ORB)).setExperience(xp);
	}
}

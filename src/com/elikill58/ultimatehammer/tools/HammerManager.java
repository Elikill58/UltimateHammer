package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.utils.Utils;

public class HammerManager extends UltimateTool implements Listener {
	
	private static final String BLOCK_METADATA = "ultimate-hammer-skip";
	private final List<Material> BLACKLIST = new ArrayList<>();
	
	public HammerManager(UltimateHammer pl) {
		super(pl, "hammer");
		List<String> list = getConfigSection().getStringList("blacklist");
		for(String s : list) {
			BLACKLIST.add(Utils.getItemFromString(s).getType());
		}
		if(BLACKLIST.size() > 0)
			pl.getLogger().info("Loaded " + BLACKLIST.size() + " blocked items for hammer.");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if(!isEnabled() || e.isCancelled())
			return;
		Player p = e.getPlayer();
		ItemStack inHand = Utils.getItemInHand(p);
		if(!isItem(inHand))
			return;
		Block baseBlock = e.getBlock();
		if(WorldRegionBypass.cannotBuild(p, this, baseBlock.getLocation()))
			return;
		if(baseBlock.hasMetadata(BLOCK_METADATA)) {
			baseBlock.removeMetadata(BLOCK_METADATA, getPlugin());
			return;
		}
		
		if(!p.getGameMode().equals(GameMode.CREATIVE))
			Utils.addDurability(inHand, (short) 2);
		Location loc = baseBlock.getLocation();
		int x1 = loc.getBlockX(), x2 = x1;
		int y1 = loc.getBlockY(), y2 = y1;
		int z1 = loc.getBlockZ(), z2 = z1;
		if(Math.abs(p.getLocation().getPitch()) <= 30) {
			y1--;
			y2++;
			float yaw = Math.abs(p.getLocation().getYaw());
			if((yaw >= 45 && yaw <= 135) || (yaw <= 315 && yaw >= 225)) {
				// move selon Z
				z1--;
				z2++;
			} else {
				// move selon X
				x1--;
				x2++;
			}
		} else {
			x1--;
			x2++;
			z1--;
			z2++;
		}
		World w = loc.getWorld();
		for(int x = x1; x <= x2; x++)
			for(int y = y1; y <= y2; y++)
				for(int z = z1; z <= z2; z++) {
					Block b = w.getBlockAt(x, y, z);
					if(b.getType().name().contains("AIR"))
						continue;
					if(BLACKLIST.contains(b.getType()))
						continue;
					if(WorldRegionBypass.cannotBuild(p, this, b.getLocation()))
						return;
					b.setMetadata(BLOCK_METADATA, new FixedMetadataValue(getPlugin(), true));
					BlockBreakEvent breakEvent = new BlockBreakEvent(b, p);
					Bukkit.getPluginManager().callEvent(breakEvent);
					if(!breakEvent.isCancelled())
						b.breakNaturally();
				}
	}
}

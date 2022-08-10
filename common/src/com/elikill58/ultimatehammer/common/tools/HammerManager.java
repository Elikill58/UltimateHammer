package com.elikill58.ultimatehammer.common.tools;

import java.util.List;

import com.elikill58.ultimatehammer.api.GameMode;
import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.utils.ItemUtils;
import com.elikill58.ultimatehammer.common.UltimateToolType;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class HammerManager extends UltimateToolType implements Listeners {
	
	public HammerManager() {
		super("hammer");
	}

	@EventListener
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled())
			return;
		Player p = e.getPlayer();
		getToolForHand(p).forEach((tool) -> {
			ItemStack inHand = p.getItemInHand();
			Block baseBlock = e.getBlock();
			if (WorldRegionBypass.cannotBuild(p, tool, baseBlock.getLocation()))
				return;

			List<Material> list = tool.getBlacklistHammer();

			if (!p.getGameMode().equals(GameMode.CREATIVE))
				ItemUtils.damage(tool, p, inHand, -1);
			Location loc = baseBlock.getLocation();
			int x1 = loc.getBlockX(), x2 = x1;
			int y1 = loc.getBlockY(), y2 = y1;
			int z1 = loc.getBlockZ(), z2 = z1;
			if (Math.abs(p.getLocation().getPitch()) <= 30) {
				y1--;
				y2++;
				float yaw = Math.abs(p.getLocation().getYaw());
				if ((yaw >= 45 && yaw <= 135) || (yaw <= 315 && yaw >= 225)) {
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
	        int next = inHand.getDurability();
			World w = loc.getWorld();
			for (int x = x1; x <= x2; x++) {
				for (int y = y1; y <= y2; y++) {
					for (int z = z1; z <= z2; z++) {
						Block b = w.getBlockAt(x, y, z);
						if (b.getType().getId().contains("AIR") || list.contains(b.getType()) || b.getType().getId().contains("WATER")
								|| b.getType().getId().contains("LAVA"))
							continue;
						if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
							continue;
						boolean notAllowed = Adapter.getAdapter().callBreakEvent(b, p);
						if(notAllowed)
							continue;
						if(!tool.usedBreak(p, b)) {
							b.breakNaturally(inHand);
				            if(next > inHand.getType().getMaxDurability()) {
				            	p.setItemInHand(null);
				            	return;
				            }
				            next++;
						}
					}
				}
			}
			ItemUtils.damage(tool, p, inHand, -1, next);
		});
	}
}

package com.elikill58.ultimatehammer.common.tools;

import java.util.List;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.Vector;
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
		getToolForHand(p).ifPresent((tool) -> {
			ItemStack inHand = p.getItemInHand();
			Block baseBlock = e.getBlock();
			if (WorldRegionBypass.cannotBuild(p, tool, baseBlock.getLocation()))
				return;
			e.setCancelled(true);
			List<Material> list = tool.getBlacklistHammer();
			Vector dir = p.getEyeLocation().getDirection().normalize();
			Location loc = baseBlock.getLocation();
			int x1 = loc.getBlockX(), x2 = x1;
			int y1 = loc.getBlockY(), y2 = y1;
			int z1 = loc.getBlockZ(), z2 = z1;
			int size = (tool.getHammerSize() - 1) / 2;

			int lowLayer = 0;
			int highLayer = 0;
			int tempLayer = tool.getHammerLayerSize() - 1;
			while (tempLayer > 0) {
				highLayer++;
				if (--tempLayer > 0) {
					lowLayer++;
					tempLayer--;
				}
			}
			if (Math.abs(p.getLocation().getPitch()) <= 30) {
				y1 -= size;
				y2 += size;
				float yaw = Math.abs(p.getLocation().getYaw());
				if ((yaw >= 45 && yaw <= 135) || (yaw <= 315 && yaw >= 225)) {
					// move selon Z
					z1 -= size;
					z2 += size;

					x1 -= dir.getX() > 0 ? lowLayer : highLayer;
					x2 += dir.getX() > 0 ? highLayer : lowLayer;
				} else {
					// move selon X
					x1 -= size;
					x2 += size;

					z1 -= dir.getZ() > 0 ? lowLayer : highLayer;
					z2 += dir.getZ() > 0 ? highLayer : lowLayer;
				}
			} else {
				x1 -= size;
				x2 += size;
				z1 -= size;
				z2 += size;

				y1 -= dir.getY() > 0 ? lowLayer : highLayer;
				y2 += dir.getY() > 0 ? highLayer : lowLayer;
			}
			int next = 0; // 1 for the first block
			World w = loc.getWorld();
			for (int x = x1; x <= x2; x++) {
				for (int y = y1; y <= y2; y++) {
					for (int z = z1; z <= z2; z++) {
						Block b = w.getBlockAt(x, y, z);
						if (b.getType().getId().contains("AIR") || list.contains(b.getType()) || b.getType().getId().contains("WATER") || b.getType().getId().contains("LAVA"))
							continue;
						if (WorldRegionBypass.cannotBuild(p, tool, b.getLocation()))
							continue;
						boolean notAllowed = Adapter.getAdapter().callBreakEvent(b, p);
						if (notAllowed)
							continue;
						if (!tool.usedBreak(p, b)) {
							b.breakNaturally(inHand);
							if (next > inHand.getType().getMaxDurability()) {
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

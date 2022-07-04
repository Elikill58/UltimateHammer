package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.utils.Utils;

public class AxeManager extends UltimateTool implements Listener {

	private static final int RANGE = 4;
	public static final List<Vector> CHECKING_VECTORS = Arrays.asList(new Vector(0, 1, 0), new Vector(0, -1, 0),
			new Vector(1, 0, 0), new Vector(0, 0, 1), new Vector(-1, 0, 0), new Vector(0, 0, -1),
			new Vector(1, 1, 0), new Vector(0, 1, 1), new Vector(-1, 1, 0), new Vector(0, 1, -1),
			new Vector(1, -1, 0), new Vector(0, -1, 1), new Vector(-1, -1, 0), new Vector(0, -1, -1));

	public AxeManager(UltimateHammer pl) {
		super(pl, "axe");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if (!isEnabled() || e.isCancelled())
			return;
		if (!isItem(Utils.getItemInHand(e.getPlayer())))
			return;
		Block b = e.getBlock();
		if (WorldRegionBypass.cannotBuild(e.getPlayer(), this, b.getLocation()))
			return;
		if (b.getType().name().contains("LOG"))
			logDetele(e);
	}

	private void logDetele(BlockBreakEvent e) {
		List<Vector> checkedPos = new ArrayList<>();
		List<Block> blist = new ArrayList<>();
		Block originBlock = e.getBlock();
		Material originType = originBlock.getType();
		Player p = e.getPlayer();
		p.giveExp(10);
		checkedPos.add(originBlock.getLocation().toVector());
		checkLeaves(p, originBlock, RANGE, checkedPos);
		blist.add(originBlock);
		AtomicInteger range = new AtomicInteger(RANGE);
		new BukkitRunnable() {
			@Override
			public void run() {
				for (int i = 0; i < blist.size(); i++) {
					Block b = blist.remove(0);
					if (b.getType().equals(originType)) {
						b.breakNaturally(Utils.getItemInHand(p));
						checkLeaves(p, b, range.get(), checkedPos);
					}
					for (Vector v : new ArrayList<>(CHECKING_VECTORS)) {
						if(checkedPos.contains(b.getLocation().clone().add(v).toVector()))
							continue;
						Block faceBlock = b.getRelative(v.getBlockX(), v.getBlockY(), v.getBlockZ());
						if (faceBlock.getType().equals(originType))
							blist.add(faceBlock);
					}

					if (blist.size() == 0) {
						cancel();
					}
				}
				range.decrementAndGet();
				if(range.get() <= 0)
					range.set(1); // keep at least 1 of range
			}
		}.runTaskTimer(getPlugin(), 0, 1);
	}

	private void checkLeaves(Player p, Block block, int range, List<Vector> checked) {
		Location loc = block.getLocation();
		final World world = loc.getWorld();
		final int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
		final int off = range;

		if (!validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
			return;

		Material leavesType = null;
		for (int offX = -range; offX <= range; offX++) {
			for (int offY = -range - 1; offY <= range + 1; offY++) { // make the Y move easier (instead of X/Z one
				for (int offZ = -range; offZ <= range; offZ++) {
					if(checked.contains(new Vector(x + offX, y + offY, z + offZ)))
						continue;
					Block offBlock = world.getBlockAt(x + offX, y + offY, z + offZ);
					if (offBlock.getType().name().contains("LEAVES")) {
						if (leavesType == null || offBlock.getType().equals(leavesType)) {
							leavesType = offBlock.getType();
							breakLeaf(p, offBlock);
						}
					}
				}
			}
		}
	}

	private void breakLeaf(Player p, Block block) {
		World world = p.getWorld();
		int x = block.getX(), y = block.getY(), z = block.getZ();

		int off = RANGE;

		if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
			for (int offX = -RANGE; offX <= RANGE; offX++) {
				for (int offY = -RANGE; offY <= RANGE; offY++) {
					for (int offZ = -RANGE; offZ <= RANGE; offZ++) {
						Material mat = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
						if (mat.name().contains("LOG")) {
							LeavesDecayEvent event = new LeavesDecayEvent(block);
							Bukkit.getServer().getPluginManager().callEvent(new LeavesDecayEvent(block));
							if (event.isCancelled())
								continue;
							if (WorldRegionBypass.cannotBuild(p, this, block.getLocation()))
								continue;
							block.breakNaturally();
							return;
						}
					}
				}
			}
		}
	}

	public boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if (maxY >= 0 && minY < world.getMaxHeight()) {
			minX >>= 4;
			minZ >>= 4;
			maxX >>= 4;
			maxZ >>= 4;

			for (int x = minX; x <= maxX; x++)
				for (int z = minZ; z <= maxZ; z++)
					if (!world.isChunkLoaded(x, z))
						return false;
			return true;
		}

		return false;
	}
}

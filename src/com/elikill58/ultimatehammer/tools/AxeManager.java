package com.elikill58.ultimatehammer.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.elikill58.ultimatehammer.UltimateHammer;
import com.elikill58.ultimatehammer.UltimateTool;
import com.elikill58.ultimatehammer.WorldRegionBypass;
import com.elikill58.ultimatehammer.utils.Utils;

public class AxeManager extends UltimateTool implements Listener {
	
	public AxeManager(UltimateHammer pl) {
		super(pl, "axe");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if(!isEnabled() || e.isCancelled())
			return;
		if(!isItem(Utils.getItemInHand(e.getPlayer())))
			return;
		if(WorldRegionBypass.cannotBuild(e.getPlayer(), this, e.getBlock().getLocation()))
			return;
		logDetele(e);
	}

	
	private void logDetele(BlockBreakEvent e) {
		Player p = e.getPlayer();
        p.giveExp(10);
        List<Block> blist = new ArrayList<>();
        checkLeaves(p, e.getBlock());
        blist.add(e.getBlock());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < blist.size(); i++) {
                    Block b = blist.get(i);
                    if (b.getType().name().contains("LOG")) {
                        for (ItemStack item : b.getDrops())
                            b.getWorld().dropItemNaturally(b.getLocation(), item);

                        b.setType(Material.AIR);
                        checkLeaves(p, b);
                    }
                    for (BlockFace face : BlockFace.values()) {
                        if (b.getRelative(face).getType().name().contains("LOG"))
                            blist.add(b.getRelative(face));
                    }

                    blist.remove(b);

                    if (blist.size() == 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(getPlugin(), 0, 1);
    }

    @SuppressWarnings("deprecation")
    private void breakLeaf(Player p, World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        byte data = block.getData();

        if ((data & 4) == 4) {
            return;
        }

        byte range = 4;
        byte max = 32;
        int[] blocks = new int[max * max * max];
        int off = range + 1;
        int mul = max * max;
        int div = max / 2;

        if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
            int offX;
            int offY;
            int offZ;
            int type;

            for (offX = -range; offX <= range; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        Material mat = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
                        if (mat.name().contains("LEAVES"))
                            type = mat.getId();
                        else if (mat.name().contains("LOG"))
                            type = mat.getId();
                        blocks[(offX + div) * mul + (offY + div) * max + offZ + div]
                        		= (mat.name().contains("LOG") ? 0  : (mat.name().contains("LEAVES") ? -2 : -1));
                    }
                }
            }

            for (offX = 1; offX <= 4; offX++) {
                for (offY = -range; offY <= range; offY++) {
                    for (offZ = -range; offZ <= range; offZ++) {
                        for (type = -range; type <= range; type++) {
                            if (blocks[(offY + div) * mul + (offZ + div) * max + type + div] == offX - 1) {
                                if (blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] == -2)
                                    blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] = offX;
                                if (blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] == -2)
                                    blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] == -2)
                                    blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] = offX;
                                if (blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] == -2)
                                    blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] = offX;
                            }
                        }
                    }
                }
            }
        }

        if (blocks[div * mul + div * max + div] < 0) {
            LeavesDecayEvent event = new LeavesDecayEvent(block);
            Bukkit.getServer().getPluginManager().callEvent(new LeavesDecayEvent(block));
            if (event.isCancelled())
                return;
            if(WorldRegionBypass.cannotBuild(p, this, block.getLocation()))
            	return;
            block.breakNaturally();
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

    private void checkLeaves(Player p, Block block) {
        Location loc = block.getLocation();
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final int range = 4;
        final int off = range + 1;

        if (!validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
            return;

        Bukkit.getServer().getScheduler().runTask(getPlugin(), new Runnable() {
            public void run() {
                for (int offX = -range; offX <= range; offX++)
                    for (int offY = -range; offY <= range; offY++)
                        for (int offZ = -range; offZ <= range; offZ++)
                            if (world.getBlockAt(x + offX, y + offY, z + offZ).getType().name().contains("LEAVES"))
                                breakLeaf(p, world, x + offX, y + offY, z + offZ);
            }
        });
    }
}

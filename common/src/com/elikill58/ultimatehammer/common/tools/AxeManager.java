package com.elikill58.ultimatehammer.common.tools;

import java.util.ArrayList;
import java.util.List;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.block.BlockFace;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.events.EventListener;
import com.elikill58.ultimatehammer.api.events.Listeners;
import com.elikill58.ultimatehammer.api.events.block.BlockBreakEvent;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.api.item.Material;
import com.elikill58.ultimatehammer.api.item.Materials;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.World;
import com.elikill58.ultimatehammer.api.utils.ItemUtils;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.common.UltimateToolType;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.bypass.WorldRegionBypass;

public class AxeManager extends UltimateToolType implements Listeners {
	
	public AxeManager() {
		super("axe");
	}
	
	@EventListener
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		Player p = e.getPlayer();
		getToolForHand(p, getKey()).forEach((tool) -> {
			p.sendMessage("Tool founded for axe");
			if(WorldRegionBypass.cannotBuild(p, tool, e.getBlock().getLocation()) || !e.getBlock().getType().getId().contains("LOG"))
				return;
	        p.giveExp(10);
	        List<Block> blist = new ArrayList<>();
	        checkLeaves(tool, p, e.getBlock());
	        blist.add(e.getBlock());
	        
	        ItemStack inHand = p.getItemInHand();
	        int next = 0;
	        while(!blist.isEmpty()) {
	            Block b = blist.remove(0);
	            if (b.getType().getId().contains("LOG")) {
	                for (ItemStack item : b.getDrops(tool.getItem()))
	                    b.getWorld().dropItemNaturally(b.getLocation(), item);

	                b.setType(Materials.AIR);
	                checkLeaves(tool, p, b);
	                next++;
	            }
	            for (BlockFace face : BlockFace.values()) {
	                if (b.getRelative(face).getType().getId().contains("LOG"))
	                    blist.add(b.getRelative(face));
	            }
	            if(next > inHand.getType().getMaxDurability()) {
	            	p.setItemInHand(null);
	            	return;
	            }
	        }
			ItemUtils.damage(tool, p, inHand, -1, next);
		});
	}

    private void breakLeaf(UltimateTool tool, Player p, World world, int x, int y, int z) {
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
                        if (mat.getId().contains("LEAVES"))
                            type = Materials.LEAVES.getIdInt();
                        else if (mat.getId().contains("LOG"))
                            type = Materials.LOG.getIdInt();
                        blocks[(offX + div) * mul + (offY + div) * max + offZ + div]
                        		= (mat.getId().contains("LOG") ? 0  : (mat.getId().contains("LEAVES") ? -2 : -1));
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
            if (Adapter.getAdapter().callLeavesDecayEvent(block))
                return;
            if(WorldRegionBypass.cannotBuild(p, tool, block.getLocation()))
            	return;
            block.breakNaturally();
        }
    }

    public boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY >= world.getMinHeight() && minY < world.getMaxHeight()) {
            minX >>= 4;
            minZ >>= 4;
            maxX >>= 4;
            maxZ >>= 4;
            // TODO add again chunk loaded check
            /*for (int x = minX; x <= maxX; x++)
                for (int z = minZ; z <= maxZ; z++)
                    if (!world.isChunkLoaded(x, z))
                        return false;*/
            return true;
        }

        return false;
    }

    private void checkLeaves(UltimateTool tool, Player p, Block block) {
        Location loc = block.getLocation();
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final int range = 4;
        final int off = range + 1;

        if (!validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off))
            return;

        Adapter.getAdapter().runSync(() -> {
            for (int offX = -range; offX <= range; offX++)
                for (int offY = -range; offY <= range; offY++)
                    for (int offZ = -range; offZ <= range; offZ++)
                        if (world.getBlockAt(x + offX, y + offY, z + offZ).getType().getId().contains("LEAVES"))
                            breakLeaf(tool, p, world, x + offX, y + offY, z + offZ);
        });
    }
}

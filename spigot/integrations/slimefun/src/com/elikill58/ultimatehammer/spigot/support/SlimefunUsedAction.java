package com.elikill58.ultimatehammer.spigot.support;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;

import com.elikill58.ultimatehammer.api.block.Block;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.common.UltimateTool;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.PluginDependentExtension;
import com.elikill58.ultimatehammer.universal.support.UsedActionManager;
import com.elikill58.ultimatehammer.universal.support.UsedActionManagerProvider;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

public class SlimefunUsedAction implements UsedActionManager {

	@Override
	public boolean usedBreak(UltimateTool tool, Player p, ItemStack item, Block b) {
		// equivalent of: https://github.com/Slimefun/Slimefun4/blob/master/src/main/java/io/github/thebusybiscuit/slimefun4/implementation/listeners/TalismanListener.java#L317
		
        ItemMeta meta = ((org.bukkit.inventory.ItemStack) item.getDefault()).getItemMeta();
        
        // Ignore Silk Touch Enchantment
        if (meta.hasEnchant(Enchantment.SILK_TOUCH)) {
            return false;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) p.getDefault();
        org.bukkit.block.Block block = (org.bukkit.block.Block) b.getDefault();
        Material type = block.getType();
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        // Handle double drops for Miner Talisman
        //doubleTalismanDrops(event, SlimefunItems.TALISMAN_MINER, SlimefunTag.MINER_TALISMAN_TRIGGERS, type, meta);

        // Handle double drops for Farmer Talisman
        doubleTalismanDrops(event, SlimefunItems.TALISMAN_FARMER, SlimefunTag.FARMER_TALISMAN_TRIGGERS, type, meta);
		return false;
	}

    private void doubleTalismanDrops(BlockBreakEvent e, SlimefunItemStack talismanItemStack, SlimefunTag tag, Material type, ItemMeta meta) {
        if (tag.isTagged(type)) {
            if (Talisman.trigger(e, talismanItemStack, false)) { // event not really called, just with cancel & player info
                int dropAmount = getAmountWithFortune(type, meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));

                // Keep track of whether we actually doubled the drops or not
                boolean doubledDrops = false;
                org.bukkit.block.Block b = e.getBlock();
                // Loop through all dropped items
                for (org.bukkit.inventory.ItemStack droppedItem : b.getDrops()) {

                    // We do not want to dupe blocks
                    if (!droppedItem.getType().isBlock()) {
                        int amount = Math.max(1, (dropAmount * 2) - droppedItem.getAmount());
                        b.getWorld().dropItemNaturally(b.getLocation(), new CustomItemStack(droppedItem, amount));
                        doubledDrops = true;
                    }
                }

                // Fixes #2077
                if (doubledDrops) {
                    Talisman talisman = talismanItemStack.getItem(Talisman.class);

                    // Fixes #2818
                    if (talisman != null) {
                        talisman.sendMessage(e.getPlayer());
                    } else
                    	e.getPlayer().sendMessage("Failed to find talisman obj");
                } else
                	e.getPlayer().sendMessage("No double drop");
            }
        } else
        	e.getPlayer().sendMessage("Type " + type.name() + " not tagged");
    }
    
    private int getAmountWithFortune(@Nonnull Material type, int fortuneLevel) {
        if (fortuneLevel > 0) {
            Random random = ThreadLocalRandom.current();
            int amount = random.nextInt(fortuneLevel + 2) - 1;
            amount = Math.max(amount, 1);
            amount = (type == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (amount + 1);
            return amount;
        } else {
            return 1;
        }
    }
	
	public static class Provider implements UsedActionManagerProvider, PluginDependentExtension {
		
		@Override
		public UsedActionManager create(Adapter adapter) {
			return new SlimefunUsedAction();
		}
		
		@Override
		public String getPluginId() {
			return "Slimefun";
		}
	}
}

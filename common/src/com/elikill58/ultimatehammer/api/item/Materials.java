package com.elikill58.ultimatehammer.api.item;

public class Materials {

    public static final Material AIR = ItemRegistrar.getInstance().get("air");

    public static final Material BARRIER = ItemRegistrar.getInstance().get("barrier", "redstone"); // redstone for 1.7
    public static final Material BEDROCK = ItemRegistrar.getInstance().get("bedrock");
    public static final Material GRASS = ItemRegistrar.getInstance().get("grass_block", "grass");
    public static final Material DIRT = ItemRegistrar.getInstance().get("dirt");
    public static final Material SOIL = ItemRegistrar.getInstance().get("soil", "farmland");
    public static final Material SOUL_SAND = ItemRegistrar.getInstance().get("soul_sand");

	public static final Material SEEDS = ItemRegistrar.getInstance().get("seeds", "wheat_seeds");
    public static final Material CROPS = ItemRegistrar.getInstance().get("crops", "wheat");
    
    public static final Material CARROT = ItemRegistrar.getInstance().get("carrot");
    public static final Material CARROT_ITEM = ItemRegistrar.getInstance().get("carrot_item", "carrots");
    
    public static final Material POTATO = ItemRegistrar.getInstance().get("potato");
    public static final Material POTATO_ITEM = ItemRegistrar.getInstance().get("potato_item", "potatoes");
    
    public static final Material NETHER_WART = ItemRegistrar.getInstance().get("nether_warts", "nether_wart");
    public static final Material NETHER_WART_ITEM = ItemRegistrar.getInstance().get("nether_stalk", "nether_wart");

    public static final Material PLAYER_HEAD = ItemRegistrar.getInstance().get("player_head", "skull_item", "skull");
    
    /* STAINED MATERIALS */
    
    // GLASS
    public static final Material GRAY_STAINED_GLASS_PANE = ItemRegistrar.getInstance().get("gray_stained_glass_pane", "stained_glass_pane");
    public static final Material RED_STAINED_GLASS_PANE = ItemRegistrar.getInstance().get("red_stained_glass_pane", "stained_glass_pane");
    // DYE
    public static final Material GRAY_DYE = ItemRegistrar.getInstance().get("gray_dye", "ink_sack", "dye");
    public static final Material LIME_DYE = ItemRegistrar.getInstance().get("lime_dye", "ink_sack", "dye");

    public static final Material LEAVES = ItemRegistrar.getInstance().get("leaves", "oak_leaves");
    public static final Material LOG = ItemRegistrar.getInstance().get("log", "oak_log");
    
    /* END OF STAINED MATERIALS */
}

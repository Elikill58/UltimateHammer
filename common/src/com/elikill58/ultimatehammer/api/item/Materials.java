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

    public static final Material CARROTS = ItemRegistrar.getInstance().get("carrots", "carrot");
    public static final Material CARROT_ITEM = ItemRegistrar.getInstance().get("carrot_item", "carrot");
    
    public static final Material CACAO = ItemRegistrar.getInstance().get("cocoa");
    public static final Material CACAO_ITEM = ItemRegistrar.getInstance().get("cocoa_beans", "cocoa");
    
    // set carrot to prevent some missing logs
    public static final Material BEETROOTS = ItemRegistrar.getInstance().get("beetroots", "carrot");
    public static final Material BEETROOT_SEEDS = ItemRegistrar.getInstance().get("beetroot_seeds", "carrot_item");

    public static final Material POTATOES = ItemRegistrar.getInstance().get("potatoes", "potato");
    public static final Material POTATO_ITEM = ItemRegistrar.getInstance().get("potato_item", "potato");
    
    public static final Material NETHER_WART = ItemRegistrar.getInstance().get("nether_warts", "nether_wart");
    public static final Material NETHER_WART_ITEM = ItemRegistrar.getInstance().get("nether_stalk", "nether_wart");

    public static final Material PLAYER_HEAD = ItemRegistrar.getInstance().get("player_head", "skull_item", "skull");
    
    public static final Material JUNGLE_LOG = ItemRegistrar.getInstance().get("jungle_log", "log");

}

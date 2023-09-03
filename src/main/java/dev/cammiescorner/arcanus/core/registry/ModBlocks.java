package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.DisplayCaseBlock;
import dev.cammiescorner.arcanus.common.blocks.FillableBookshelfBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModBlocks {
    //-----Block Map-----//
    public static final LinkedHashMap<Block, Identifier> BLOCKS = new LinkedHashMap<>();

    //-----Blocks-----//
    public static final Block BOOKSHELF = create("fillable_bookshelf", new FillableBookshelfBlock(FabricBlockSettings.copyOf(Blocks.BOOKSHELF)));
    public static final Block DISPLAY_CASE = create("display_case", new DisplayCaseBlock(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE).requiresTool().nonOpaque()));

    //-----Registry-----//
    public static void register() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
        BLOCKS.keySet().forEach(block -> Registry.register(Registries.ITEM, BLOCKS.get(block), getItem(block)));
    }

    private static BlockItem getItem(Block block) {
        return new BlockItem(block, new Item.Settings());
    }

    private static <T extends Block> T create(String name, T block) {
        BLOCKS.put(block, new Identifier(Arcanus.MOD_ID, name));
        return block;
    }
}

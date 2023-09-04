package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.DisplayCaseBlock;
import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;

public class ArcanusBlocks {

    public static final LinkedHashMap<Block, Identifier> BLOCKS = new LinkedHashMap<>();

    public static final Block BOOKSHELF = create("fillable_bookshelf", new FillableBookshelfBlock(QuiltBlockSettings.copyOf(Blocks.BOOKSHELF)));
    public static final Block DISPLAY_CASE = create("display_case", new DisplayCaseBlock(QuiltBlockSettings.copyOf(Blocks.SMOOTH_STONE).requiresTool().nonOpaque()));

    public static void register() {
        BLOCKS.forEach((block, id) -> {
            Registry.register(Registries.BLOCK, id, block);
            Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        });
    }

    private static <T extends Block> T create(String name, T block) {
        BLOCKS.put(block, Arcanus.id(name));
        return block;
    }
}

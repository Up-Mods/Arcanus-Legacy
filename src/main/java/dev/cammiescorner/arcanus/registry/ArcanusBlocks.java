package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.DisplayCaseBlock;
import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.LinkedHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ArcanusBlocks {

    public static final LinkedHashMap<Block, ResourceLocation> BLOCKS = new LinkedHashMap<>();

    public static final Block BOOKSHELF = create("fillable_bookshelf", new FillableBookshelfBlock(QuiltBlockSettings.copyOf(Blocks.BOOKSHELF)));
    public static final Block DISPLAY_CASE = create("display_case", new DisplayCaseBlock(QuiltBlockSettings.copyOf(Blocks.SMOOTH_STONE).requiresCorrectToolForDrops().noOcclusion()));

    public static void register() {
        BLOCKS.forEach((block, id) -> {
            Registry.register(BuiltInRegistries.BLOCK, id, block);
            Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties()));
        });
    }

    private static <T extends Block> T create(String name, T block) {
        BLOCKS.put(block, Arcanus.id(name));
        return block;
    }
}

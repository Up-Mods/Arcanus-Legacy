package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.DisplayCaseBlock;
import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class ArcanusBlocks {

    public static final RegistryHandler<Block> BLOCKS = RegistryHandler.create(Registries.BLOCK, Arcanus.MODID);

    public static final RegistrySupplier<Block> BOOKSHELF = BLOCKS.register("fillable_bookshelf", () -> new FillableBookshelfBlock(QuiltBlockSettings.copyOf(Blocks.BOOKSHELF)));
    public static final RegistrySupplier<Block> DISPLAY_CASE = BLOCKS.register("display_case", () -> new DisplayCaseBlock(QuiltBlockSettings.copyOf(Blocks.SMOOTH_STONE).requiresCorrectToolForDrops().noOcclusion()));

}

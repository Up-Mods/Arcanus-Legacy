package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.entity.DisplayCaseBlockEntity;
import dev.cammiescorner.arcanus.block.entity.FillableBookshelfBlockEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ArcanusBlockEntities {

    public static final RegistryHandler<BlockEntityType<?>> BLOCK_ENTITIES = RegistryHandler.create(Registries.BLOCK_ENTITY_TYPE, Arcanus.MODID);

    public static final RegistrySupplier<BlockEntityType<DisplayCaseBlockEntity>> DISPLAY_CASE = BLOCK_ENTITIES.register("display_case", () -> QuiltBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, ArcanusBlocks.DISPLAY_CASE.get()).build());
    public static final RegistrySupplier<BlockEntityType<FillableBookshelfBlockEntity>> FILLABLE_BOOKSHELF = BLOCK_ENTITIES.register("fillable_bookshelf", () -> QuiltBlockEntityTypeBuilder.create(FillableBookshelfBlockEntity::new, ArcanusBlocks.BOOKSHELF.get()).build());




}

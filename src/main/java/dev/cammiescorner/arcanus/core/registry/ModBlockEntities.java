package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.entities.DisplayCaseBlockEntity;
import dev.cammiescorner.arcanus.common.blocks.entities.FillableBookshelfBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ModBlockEntities {

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Arcanus.id("display_case"), DISPLAY_CASE);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Arcanus.id("fillable_bookshelf"), FILLABLE_BOOKSHELF);
    }

    public static final BlockEntityType<DisplayCaseBlockEntity> DISPLAY_CASE = QuiltBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, ModBlocks.DISPLAY_CASE).build();
    public static final BlockEntityType<FillableBookshelfBlockEntity> FILLABLE_BOOKSHELF = QuiltBlockEntityTypeBuilder.create(FillableBookshelfBlockEntity::new, ModBlocks.BOOKSHELF).build();




}

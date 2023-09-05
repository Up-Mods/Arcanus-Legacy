package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.entity.DisplayCaseBlockEntity;
import dev.cammiescorner.arcanus.block.entity.FillableBookshelfBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ArcanusBlockEntities {

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Arcanus.id("display_case"), DISPLAY_CASE);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Arcanus.id("fillable_bookshelf"), FILLABLE_BOOKSHELF);
    }

    public static final BlockEntityType<DisplayCaseBlockEntity> DISPLAY_CASE = QuiltBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, ArcanusBlocks.DISPLAY_CASE).build();
    public static final BlockEntityType<FillableBookshelfBlockEntity> FILLABLE_BOOKSHELF = QuiltBlockEntityTypeBuilder.create(FillableBookshelfBlockEntity::new, ArcanusBlocks.BOOKSHELF).build();




}

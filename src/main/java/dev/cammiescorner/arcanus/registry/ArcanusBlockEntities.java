package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.block.entity.DisplayCaseBlockEntity;
import dev.cammiescorner.arcanus.block.entity.FillableBookshelfBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ArcanusBlockEntities {

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Arcanus.id("display_case"), DISPLAY_CASE);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Arcanus.id("fillable_bookshelf"), FILLABLE_BOOKSHELF);
    }

    public static final BlockEntityType<DisplayCaseBlockEntity> DISPLAY_CASE = QuiltBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, ArcanusBlocks.DISPLAY_CASE).build();
    public static final BlockEntityType<FillableBookshelfBlockEntity> FILLABLE_BOOKSHELF = QuiltBlockEntityTypeBuilder.create(FillableBookshelfBlockEntity::new, ArcanusBlocks.BOOKSHELF).build();




}

package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.entities.DisplayCaseBlockEntity;
import dev.cammiescorner.arcanus.common.blocks.entities.FillableBookshelfBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModBlockEntities {
	//-----Block Entity Type Map-----//
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	//-----Block Entity Types-----//
	public static final BlockEntityType<FillableBookshelfBlockEntity> FILLABLE_BOOKSHELF = create("fillable_bookshelf", FabricBlockEntityTypeBuilder.create(FillableBookshelfBlockEntity::new, ModBlocks.BOOKSHELF).build());
	public static final BlockEntityType<DisplayCaseBlockEntity> DISPLAY_CASE = create("display_case", FabricBlockEntityTypeBuilder.create(DisplayCaseBlockEntity::new, ModBlocks.DISPLAY_CASE).build());

	//-----Registry-----//
	public static void register() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registries.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}

	private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new Identifier(Arcanus.MOD_ID, name));
		return type;
	}
}

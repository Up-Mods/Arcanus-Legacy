package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArcanusBlockEntities {
	//-----Block Entity Type Map-----//
	private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	//-----Block Entity Types-----//
	public static final BlockEntityType<AmethystAltarBlockEntity> AMETHYST_ALTAR = create("amethyst_altar", FabricBlockEntityTypeBuilder.create(AmethystAltarBlockEntity::new, ArcanusBlocks.AMETHYST_ALTAR).build());

	//-----Registry-----//
	public static void register() {
		BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
	}

	private static <T extends BlockEntity> BlockEntityType<T> create(String name, BlockEntityType<T> type) {
		BLOCK_ENTITY_TYPES.put(type, new Identifier(Arcanus.MOD_ID, name));
		return type;
	}
}

package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModEntities {
	//-----Entity Map-----//
	public static final LinkedHashMap<EntityType, Identifier> ENTITIES = new LinkedHashMap<>();

	//-----Entities-----//
	public static final EntityType<SolarStrikeEntity> SOLAR_STRIKE = create("solar_strike",
			FabricEntityTypeBuilder.<SolarStrikeEntity>create(SpawnGroup.MISC, (type, world) -> new SolarStrikeEntity(world))
					.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
					.trackRangeChunks(640)
					.build());

	//-----Registry-----//
	public static void register() {
		ENTITIES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
	}

	private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
		ENTITIES.put(type, new Identifier(Arcanus.MOD_ID, name));
		return type;
	}
}

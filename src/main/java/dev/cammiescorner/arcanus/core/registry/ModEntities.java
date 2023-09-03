package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.common.entities.MagicMissileEntity;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModEntities {
    //-----Entity Map-----//
    public static final LinkedHashMap<EntityType<?>, Identifier> ENTITIES = new LinkedHashMap<>();

    //-----Entities-----//
    public static final EntityType<SolarStrikeEntity> SOLAR_STRIKE = create("solar_strike",
            FabricEntityTypeBuilder.<SolarStrikeEntity>create(SpawnGroup.MISC, SolarStrikeEntity::new)
                    .dimensions(EntityDimensions.fixed(0F, 0F))
                    .trackRangeChunks(640)
                    .build());
    public static final EntityType<ArcaneBarrierEntity> ARCANE_BARRIER = create("arcane_barrier",
            FabricEntityTypeBuilder.<ArcaneBarrierEntity>create(SpawnGroup.MISC, ArcaneBarrierEntity::new)
                    .dimensions(EntityDimensions.changing(1F, 0F))
                    .build());
    public static final EntityType<MagicMissileEntity> MAGIC_MISSILE = create("magic_missile",
            FabricEntityTypeBuilder.<MagicMissileEntity>create(SpawnGroup.MISC, MagicMissileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeChunks(64)
                    .build());

    //-----Registry-----//
    public static void register() {
        ENTITIES.keySet().forEach(entityType -> Registry.register(Registries.ENTITY_TYPE, ENTITIES.get(entityType), entityType));
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
        ENTITIES.put(type, new Identifier(Arcanus.MOD_ID, name));
        return type;
    }
}

package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.common.entities.MagicMissileEntity;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import java.util.LinkedHashMap;

public class ModEntities {

    public static final LinkedHashMap<EntityType<?>, Identifier> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<SolarStrikeEntity> SOLAR_STRIKE = create("solar_strike",
            QuiltEntityTypeBuilder.<SolarStrikeEntity>create(SpawnGroup.MISC, SolarStrikeEntity::new)
                    .setDimensions(EntityDimensions.fixed(0F, 0F))
                    .maxChunkTrackingRange(640)
                    .build());
    public static final EntityType<ArcaneBarrierEntity> ARCANE_BARRIER = create("arcane_barrier",
            QuiltEntityTypeBuilder.<ArcaneBarrierEntity>create(SpawnGroup.MISC, ArcaneBarrierEntity::new)
                    .setDimensions(EntityDimensions.changing(1F, 0F))
                    .build());
    public static final EntityType<MagicMissileEntity> MAGIC_MISSILE = create("magic_missile",
            QuiltEntityTypeBuilder.<MagicMissileEntity>create(SpawnGroup.MISC, MagicMissileEntity::new)
                    .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .maxChunkTrackingRange(64)
                    .build());

    public static void register() {
        ENTITIES.forEach((entityType, id) -> Registry.register(Registries.ENTITY_TYPE, id, entityType));
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
        ENTITIES.put(type, Arcanus.id(name));
        return type;
    }
}

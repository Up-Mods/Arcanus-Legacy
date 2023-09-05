package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import java.util.LinkedHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ArcanusEntities {

    public static final LinkedHashMap<EntityType<?>, ResourceLocation> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<SolarStrikeEntity> SOLAR_STRIKE = create("solar_strike",
            QuiltEntityTypeBuilder.<SolarStrikeEntity>create(MobCategory.MISC, SolarStrikeEntity::new)
                    .setDimensions(EntityDimensions.fixed(0F, 0F))
                    .maxChunkTrackingRange(640)
                    .build());
    public static final EntityType<ArcaneBarrierEntity> ARCANE_BARRIER = create("arcane_barrier",
            QuiltEntityTypeBuilder.<ArcaneBarrierEntity>create(MobCategory.MISC, ArcaneBarrierEntity::new)
                    .setDimensions(EntityDimensions.scalable(1F, 0F))
                    .build());
    public static final EntityType<MagicMissileEntity> MAGIC_MISSILE = create("magic_missile",
            QuiltEntityTypeBuilder.<MagicMissileEntity>create(MobCategory.MISC, MagicMissileEntity::new)
                    .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .maxChunkTrackingRange(64)
                    .build());

    public static void register() {
        ENTITIES.forEach((entityType, id) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id, entityType));
    }

    private static <T extends Entity> EntityType<T> create(String name, EntityType<T> type) {
        ENTITIES.put(type, Arcanus.id(name));
        return type;
    }
}

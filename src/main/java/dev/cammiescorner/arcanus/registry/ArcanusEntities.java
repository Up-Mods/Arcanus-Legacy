package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class ArcanusEntities {

    public static final RegistryHandler<EntityType<?>> ENTITIES = RegistryHandler.create(Registries.ENTITY_TYPE, Arcanus.MODID);

    public static final RegistrySupplier<EntityType<SolarStrikeEntity>> SOLAR_STRIKE = ENTITIES.register("solar_strike",
            () -> QuiltEntityTypeBuilder.<SolarStrikeEntity>create(MobCategory.MISC, SolarStrikeEntity::new)
                    .setDimensions(EntityDimensions.fixed(0F, 0F))
                    .maxChunkTrackingRange(640)
                    .build());
    public static final RegistrySupplier<EntityType<ArcaneBarrierEntity>> ARCANE_BARRIER = ENTITIES.register("arcane_barrier",
            () -> QuiltEntityTypeBuilder.<ArcaneBarrierEntity>create(MobCategory.MISC, ArcaneBarrierEntity::new)
                    .setDimensions(EntityDimensions.scalable(1F, 0F))
                    .build());
    public static final RegistrySupplier<EntityType<MagicMissileEntity>> MAGIC_MISSILE = ENTITIES.register("magic_missile",
            () -> QuiltEntityTypeBuilder.<MagicMissileEntity>create(MobCategory.MISC, MagicMissileEntity::new)
                    .setDimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .maxChunkTrackingRange(64)
                    .build());
}

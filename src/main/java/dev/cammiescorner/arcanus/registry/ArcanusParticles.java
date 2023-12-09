package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ArcanusParticles {

    public static final RegistryHandler<ParticleType<?>> PARTICLES = RegistryHandler.create(Registries.PARTICLE_TYPE, Arcanus.MODID);

    public static final RegistrySupplier<SimpleParticleType> MAGIC_MISSILE = PARTICLES.register("magic_missile", FabricParticleTypes::simple);
    public static final RegistrySupplier<SimpleParticleType> TELEKINETIC_SHOCK = PARTICLES.register("telekinetic_shock", FabricParticleTypes::simple);
    public static final RegistrySupplier<SimpleParticleType> HEAL = PARTICLES.register("heal", FabricParticleTypes::simple);
    public static final RegistrySupplier<SimpleParticleType> DISCOMBOBULATE = PARTICLES.register("discombobulate", FabricParticleTypes::simple);

}

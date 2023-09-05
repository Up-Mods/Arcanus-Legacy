package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import java.util.LinkedHashMap;

public class ArcanusParticles {

    public static final LinkedHashMap<ParticleType<?>, ResourceLocation> PARTICLE_TYPES = new LinkedHashMap<>();

    public static final ParticleType<SimpleParticleType> MAGIC_MISSILE = create("magic_missile", FabricParticleTypes.simple());
    public static final ParticleType<SimpleParticleType> TELEKINETIC_SHOCK = create("telekinetic_shock", FabricParticleTypes.simple());
    public static final ParticleType<SimpleParticleType> HEAL = create("heal", FabricParticleTypes.simple());
    public static final ParticleType<SimpleParticleType> DISCOMBOBULATE = create("discombobulate", FabricParticleTypes.simple());

    public static void register() {
        PARTICLE_TYPES.forEach((particleType, id) -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, id, particleType));
    }

    private static <T extends ParticleOptions> ParticleType<T> create(String name, ParticleType<T> type) {
        PARTICLE_TYPES.put(type, Arcanus.id(name));
        return type;
    }
}

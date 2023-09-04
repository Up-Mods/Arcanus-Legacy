package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModParticles {

    public static final LinkedHashMap<ParticleType<?>, Identifier> PARTICLE_TYPES = new LinkedHashMap<>();

    public static final ParticleType<DefaultParticleType> MAGIC_MISSILE = create("magic_missile", FabricParticleTypes.simple());
    public static final ParticleType<DefaultParticleType> TELEKINETIC_SHOCK = create("telekinetic_shock", FabricParticleTypes.simple());
    public static final ParticleType<DefaultParticleType> HEAL = create("heal", FabricParticleTypes.simple());
    public static final ParticleType<DefaultParticleType> DISCOMBOBULATE = create("discombobulate", FabricParticleTypes.simple());

    public static void register() {
        PARTICLE_TYPES.forEach((particleType, id) -> Registry.register(Registries.PARTICLE_TYPE, id, particleType));
    }

    private static <T extends ParticleEffect> ParticleType<T> create(String name, ParticleType<T> type) {
        PARTICLE_TYPES.put(type, Arcanus.id(name));
        return type;
    }
}

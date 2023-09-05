package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArcanusDamageTypes {

    public static final ResourceKey<DamageType> MAGIC_BURNOUT = ResourceKey.create(Registries.DAMAGE_TYPE, Arcanus.id("magic_burnout"));
    public static final ResourceKey<DamageType> SOLAR_STRIKE = ResourceKey.create(Registries.DAMAGE_TYPE, Arcanus.id("solar_strike"));

    public static DamageSource burnout(Level world) {
        return world.damageSources().source(MAGIC_BURNOUT);
    }

    public static DamageSource solarStrike(SolarStrikeEntity source, @Nullable Entity attacker) {
        return source.damageSources().source(SOLAR_STRIKE, source, attacker);
    }
}

package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {

	public static final RegistryKey<DamageType> MAGIC_BURNOUT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Arcanus.id("magic_burnout"));
	public static final RegistryKey<DamageType> SOLAR_STRIKE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Arcanus.id("solar_strike"));

	public static DamageSource burnout(World world) {
		return world.getDamageSources().create(MAGIC_BURNOUT);
	}

	public static DamageSource solarStrike(SolarStrikeEntity source, @Nullable Entity attacker) {
		return source.getDamageSources().create(SOLAR_STRIKE, source, attacker);
	}
}

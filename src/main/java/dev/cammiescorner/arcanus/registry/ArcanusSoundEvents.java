package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class ArcanusSoundEvents {

    public static final RegistryHandler<SoundEvent> SOUND_EVENTS = RegistryHandler.create(Registries.SOUND_EVENT, Arcanus.MODID);

    public static final RegistrySupplier<SoundEvent> MAGIC_MISSILE = SOUND_EVENTS.register("magic_missile",()->SoundEvent.createVariableRangeEvent(Arcanus.id("magic_missile")));
    public static final RegistrySupplier<SoundEvent> TELEKINETIC_SHOCK = SOUND_EVENTS.register("telekinetic_shock",()->SoundEvent.createVariableRangeEvent(Arcanus.id("telekinetic_shock")));
    public static final RegistrySupplier<SoundEvent> SOLAR_STRIKE = SOUND_EVENTS.register("solar_strike",()->SoundEvent.createVariableRangeEvent(Arcanus.id("solar_strike")));
    public static final RegistrySupplier<SoundEvent> HEAL = SOUND_EVENTS.register("heal",()->SoundEvent.createVariableRangeEvent(Arcanus.id("heal")));
}

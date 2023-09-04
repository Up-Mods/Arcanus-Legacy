package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModSoundEvents {

    public static final LinkedHashMap<SoundEvent, Identifier> SOUNDS = new LinkedHashMap<>();

    public static final SoundEvent MAGIC_MISSILE = create("magic_missile");
    public static final SoundEvent TELEKINETIC_SHOCK = create("telekinetic_shock");
    public static final SoundEvent SOLAR_STRIKE = create("solar_strike");
    public static final SoundEvent HEAL = create("heal");

    public static void register() {
        SOUNDS.forEach((sound, id) -> Registry.register(Registries.SOUND_EVENT, id, sound));
    }

    private static SoundEvent create(String name) {
        SoundEvent sound = SoundEvent.createVariableRangeEvent(Arcanus.id(name));
        SOUNDS.put(sound, Arcanus.id(name));
        return sound;
    }
}

package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import java.util.LinkedHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ArcanusSoundEvents {

    public static final LinkedHashMap<SoundEvent, ResourceLocation> SOUNDS = new LinkedHashMap<>();

    public static final SoundEvent MAGIC_MISSILE = create("magic_missile");
    public static final SoundEvent TELEKINETIC_SHOCK = create("telekinetic_shock");
    public static final SoundEvent SOLAR_STRIKE = create("solar_strike");
    public static final SoundEvent HEAL = create("heal");

    public static void register() {
        SOUNDS.forEach((sound, id) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, sound));
    }

    private static SoundEvent create(String name) {
        SoundEvent sound = SoundEvent.createVariableRangeEvent(Arcanus.id(name));
        SOUNDS.put(sound, Arcanus.id(name));
        return sound;
    }
}

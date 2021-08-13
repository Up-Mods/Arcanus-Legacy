package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModSoundEvents {
	//-----Sound Map-----//
	public static final LinkedHashMap<SoundEvent, Identifier> SOUNDS = new LinkedHashMap<>();

	//-----Sound Events-----//
	public static final SoundEvent MAGIC_MISSILE = create("magic_missile");
	public static final SoundEvent TELEKINETIC_SHOCK = create("telekinetic_shock");
	public static final SoundEvent SOLAR_STRIKE = create("solar_strike");
	public static final SoundEvent HEAL = create("heal");

	//-----Registry-----//
	public static void register() {
		SOUNDS.keySet().forEach(sound -> Registry.register(Registry.SOUND_EVENT, SOUNDS.get(sound), sound));
	}

	private static SoundEvent create(String name) {
		SoundEvent sound = new SoundEvent(new Identifier(Arcanus.MOD_ID, name));
		SOUNDS.put(sound, new Identifier(Arcanus.MOD_ID, name));
		return sound;
	}
}

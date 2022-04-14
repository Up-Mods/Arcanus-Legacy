package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class ArcanusSounds {
	public static final SoundEvent BLOCK_AMETHYST_CRYSTAL_GROW = new SoundEvent(Arcanus.id("block.amethyst_block.grow"));

	public static void register() {
		Registry.register(Registry.SOUND_EVENT, BLOCK_AMETHYST_CRYSTAL_GROW.getId(), BLOCK_AMETHYST_CRYSTAL_GROW);
	}
}

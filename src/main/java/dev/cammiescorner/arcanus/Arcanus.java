package dev.cammiescorner.arcanus;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Arcanus implements ModInitializer {
	public static final String MOD_ID = "arcanus";

	@Override
	public void onInitialize() {
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "casting_multiplier"), EntityAttributes.AURA_COST);
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "mana_regen"), EntityAttributes.AURA_REGEN);
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static class EntityAttributes {
		public static final EntityAttribute AURA_COST = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".aura_cost", 1D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute AURA_REGEN = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".aura_regen", 60D, 0D, 1024D).setTracked(true);
	}
}

package dev.cammiescorner.arcanus.api.entity;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

public class ArcanusAttributes {
	public static final EntityAttribute AURA_COST = new ClampedEntityAttribute(id("aura_cost"), 1D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute AURA_REGEN = new ClampedEntityAttribute(id("aura_regen"), 60D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute AURA_LOCK = new ClampedEntityAttribute(id("aura_lock"), 0D, 0D, 20D).setTracked(true);

	public static final EntityAttribute ENHANCEMENT_AFFINITY = new ClampedEntityAttribute(id("enhancement_affinity"), 0.5D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute TRANSMUTATION_AFFINITY = new ClampedEntityAttribute(id("transmutation_affinity"), 0.5D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute EMISSION_AFFINITY = new ClampedEntityAttribute(id("emission_affinity"), 0.5D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute CONJURATION_AFFINITY = new ClampedEntityAttribute(id("conjuration_affinity"), 0.5D, 0D, 1024D).setTracked(true);
	public static final EntityAttribute MANIPULATION_AFFINITY = new ClampedEntityAttribute(id("manipulation_affinity"), 0.5D, 0D, 1024D).setTracked(true);

	private static String id(String name) {
		return "attribute.name.generic." + Arcanus.MOD_ID + "." + name;
	}
}

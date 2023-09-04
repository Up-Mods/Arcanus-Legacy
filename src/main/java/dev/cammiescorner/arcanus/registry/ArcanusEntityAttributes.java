package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ArcanusEntityAttributes {

    public static final EntityAttribute MANA_COST = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_cost"), 1D, 0D, 1024D).setTracked(true);
    public static final EntityAttribute MANA_REGEN = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_regen"), 1D, 0D, 1024D).setTracked(true);
    public static final EntityAttribute BURNOUT_REGEN = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "burnout_regen"), 1D, 0D, 1024D).setTracked(true);
    public static final EntityAttribute MANA_LOCK = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_lock"), 0D, 0D, 20D).setTracked(true);

    public static void register() {
        Registry.register(Registries.ENTITY_ATTRIBUTE, Arcanus.id("casting_multiplier"), MANA_COST);
        Registry.register(Registries.ENTITY_ATTRIBUTE, Arcanus.id("mana_regen"), MANA_REGEN);
        Registry.register(Registries.ENTITY_ATTRIBUTE, Arcanus.id("burnout_regen"), BURNOUT_REGEN);
        Registry.register(Registries.ENTITY_ATTRIBUTE, Arcanus.id("mana_lock"), MANA_LOCK);
    }
}

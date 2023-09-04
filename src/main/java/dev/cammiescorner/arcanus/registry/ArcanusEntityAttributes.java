package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;

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

    public static double getManaCost(PlayerEntity player) {
        @Nullable final EntityAttributeInstance castingMultiplier = player.getAttributeInstance(MANA_COST);
        return castingMultiplier != null ? castingMultiplier.getValue() : 1D;
    }

    public static double getManaRegen(PlayerEntity player) {
        @Nullable final EntityAttributeInstance manaRegen = player.getAttributeInstance(MANA_REGEN);
        return manaRegen != null ? manaRegen.getValue() : 1D;
    }

    public static double getBurnoutRegen(PlayerEntity player) {
        @Nullable final EntityAttributeInstance burnoutRegen = player.getAttributeInstance(BURNOUT_REGEN);
        return burnoutRegen != null ? burnoutRegen.getValue() : 1D;
    }

    public static int getManaLock(PlayerEntity player) {
        @Nullable final EntityAttributeInstance manaLock = player.getAttributeInstance(MANA_LOCK);
        return (int) (manaLock != null ? manaLock.getValue() : 0D);
    }
}

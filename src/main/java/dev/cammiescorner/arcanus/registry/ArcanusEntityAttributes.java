package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ArcanusEntityAttributes {

    public static final Attribute MANA_COST = new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_cost"), 1D, 0D, 1024D).setSyncable(true);
    public static final Attribute MANA_REGEN = new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_regen"), 1D, 0D, 1024D).setSyncable(true);
    public static final Attribute BURNOUT_REGEN = new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "burnout_regen"), 1D, 0D, 1024D).setSyncable(true);
    public static final Attribute MANA_LOCK = new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_lock"), 0D, 0D, 20D).setSyncable(true);

    public static void register() {
        Registry.register(BuiltInRegistries.ATTRIBUTE, Arcanus.id("casting_multiplier"), MANA_COST);
        Registry.register(BuiltInRegistries.ATTRIBUTE, Arcanus.id("mana_regen"), MANA_REGEN);
        Registry.register(BuiltInRegistries.ATTRIBUTE, Arcanus.id("burnout_regen"), BURNOUT_REGEN);
        Registry.register(BuiltInRegistries.ATTRIBUTE, Arcanus.id("mana_lock"), MANA_LOCK);
    }

    public static double getManaCost(Player player) {
        @Nullable final AttributeInstance castingMultiplier = player.getAttribute(MANA_COST);
        return castingMultiplier != null ? castingMultiplier.getValue() : 1D;
    }

    public static double getManaRegen(Player player) {
        @Nullable final AttributeInstance manaRegen = player.getAttribute(MANA_REGEN);
        return manaRegen != null ? manaRegen.getValue() : 1D;
    }

    public static double getBurnoutRegen(Player player) {
        @Nullable final AttributeInstance burnoutRegen = player.getAttribute(BURNOUT_REGEN);
        return burnoutRegen != null ? burnoutRegen.getValue() : 1D;
    }

    public static int getManaLock(Player player) {
        @Nullable final AttributeInstance manaLock = player.getAttribute(MANA_LOCK);
        return (int) (manaLock != null ? manaLock.getValue() : 0D);
    }
}

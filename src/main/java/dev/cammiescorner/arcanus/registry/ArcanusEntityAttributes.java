package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class ArcanusEntityAttributes {

    @ApiStatus.Internal
    public static RegistryHandler<Attribute> ATTRIBUTES = RegistryHandler.create(Registries.ATTRIBUTE, Arcanus.MODID);
    private static boolean initialized = false;

    public static final RegistrySupplier<Attribute> MANA_COST = ATTRIBUTES.register("mana_cost", () -> new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_cost"), 1D, 0D, 1024D).setSyncable(true));
    public static final RegistrySupplier<Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen", () -> new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_regen"), 1D, 0D, 1024D).setSyncable(true));
    public static final RegistrySupplier<Attribute> BURNOUT_REGEN = ATTRIBUTES.register("burnout_regen", () -> new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "burnout_regen"), 1D, 0D, 1024D).setSyncable(true));
    public static final RegistrySupplier<Attribute> MANA_LOCK = ATTRIBUTES.register("mana_lock", () -> new RangedAttribute(Arcanus.translationKey("attribute.name.generic", "mana_lock"), 0D, 0D, 20D).setSyncable(true));

    public static void register(RegistryService registryService) {
        if (isInitialized()) return;
        ATTRIBUTES.accept(registryService);
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static double getManaCost(Player player) {
        @Nullable final AttributeInstance castingMultiplier = player.getAttribute(MANA_COST.get());
        return castingMultiplier != null ? castingMultiplier.getValue() : 1D;
    }

    public static double getManaRegen(Player player) {
        @Nullable final AttributeInstance manaRegen = player.getAttribute(MANA_REGEN.get());
        return manaRegen != null ? manaRegen.getValue() : 1D;
    }

    public static double getBurnoutRegen(Player player) {
        @Nullable final AttributeInstance burnoutRegen = player.getAttribute(BURNOUT_REGEN.get());
        return burnoutRegen != null ? burnoutRegen.getValue() : 1D;
    }

    public static int getManaLock(Player player) {
        @Nullable final AttributeInstance manaLock = player.getAttribute(MANA_LOCK.get());
        return (int) (manaLock != null ? manaLock.getValue() : 0D);
    }
}

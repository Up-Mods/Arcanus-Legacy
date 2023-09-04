package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ArcanusLootFunctions {

    public static void register() {
        Registry.register(Registries.LOOK_FUNCTION_TYPE, Arcanus.id("arcanus_loot_function"), ARCANUS_LOOT_FUNCTION);
    }
}

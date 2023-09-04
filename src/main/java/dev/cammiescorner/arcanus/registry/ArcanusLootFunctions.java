package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.loot.function.SetSpellBookNbtLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ArcanusLootFunctions {

    public static final LootFunctionType SET_SPELL_BOOK_NBT = new LootFunctionType(new SetSpellBookNbtLootFunction.Serializer());

    public static void register() {
        Registry.register(Registries.LOOK_FUNCTION_TYPE, Arcanus.id("set_spell_book_nbt"), SET_SPELL_BOOK_NBT);
    }
}

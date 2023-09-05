package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.loot.function.SetSpellBookNbtLootFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ArcanusLootFunctions {

    public static final LootItemFunctionType SET_SPELL_BOOK_NBT = new LootItemFunctionType(new SetSpellBookNbtLootFunction.Serializer());

    public static void register() {
        Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Arcanus.id("set_spell_book_nbt"), SET_SPELL_BOOK_NBT);
    }
}

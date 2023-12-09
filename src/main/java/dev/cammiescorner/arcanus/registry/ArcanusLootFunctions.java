package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.loot.function.SetSpellBookNbtLootFunction;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ArcanusLootFunctions {

    public static final RegistryHandler<LootItemFunctionType> LOOT_FUNCTIONS = RegistryHandler.create(Registries.LOOT_FUNCTION_TYPE, Arcanus.MODID);

    public static final RegistrySupplier<LootItemFunctionType> SET_SPELL_BOOK_NBT = LOOT_FUNCTIONS.register("set_spell_book_nbt", () -> new LootItemFunctionType(new SetSpellBookNbtLootFunction.Serializer()));
}

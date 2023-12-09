package dev.cammiescorner.arcanus.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.cammiescorner.arcanus.registry.ArcanusLootFunctions;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetSpellBookNbtLootFunction extends LootItemConditionalFunction {
    protected SetSpellBookNbtLootFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        return SpellBooks.getRandomSpellBook(stack, context.getRandom());
    }

    @Override
    public LootItemFunctionType getType() {
        return ArcanusLootFunctions.SET_SPELL_BOOK_NBT.get();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetSpellBookNbtLootFunction.Builder> {
        @Override
        protected SetSpellBookNbtLootFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetSpellBookNbtLootFunction(this.getConditions());
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetSpellBookNbtLootFunction> {
        @Override
        public SetSpellBookNbtLootFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new SetSpellBookNbtLootFunction(conditions);
        }
    }
}

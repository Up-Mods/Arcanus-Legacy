package dev.cammiescorner.arcanus.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.cammiescorner.arcanus.registry.ArcanusLootFunctions;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class SetSpellBookNbtLootFunction extends ConditionalLootFunction {
    protected SetSpellBookNbtLootFunction(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        return SpellBooks.getRandomSpellBook(stack, context.getRandom());
    }

    @Override
    public LootFunctionType getType() {
        return ArcanusLootFunctions.SET_SPELL_BOOK_NBT;
    }

    public static class Builder extends ConditionalLootFunction.Builder<SetSpellBookNbtLootFunction.Builder> {
        @Override
        protected SetSpellBookNbtLootFunction.Builder getThisBuilder() {
            return this;
        }

        @Override
        public LootFunction build() {
            return new SetSpellBookNbtLootFunction(this.getConditions());
        }
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetSpellBookNbtLootFunction> {
        @Override
        public SetSpellBookNbtLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new SetSpellBookNbtLootFunction(conditions);
        }
    }
}

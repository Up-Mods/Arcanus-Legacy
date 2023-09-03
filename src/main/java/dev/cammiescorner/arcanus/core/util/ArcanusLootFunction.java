package dev.cammiescorner.arcanus.core.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class ArcanusLootFunction extends ConditionalLootFunction {
	protected ArcanusLootFunction(LootCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ItemStack process(ItemStack stack, LootContext context) {
		return SpellBooks.getRandomSpellBook(stack, context.getRandom());
	}

	@Override
	public LootFunctionType getType() {
		return Arcanus.ARCANUS_LOOT_FUNCTION;
	}

	public static class Builder extends ConditionalLootFunction.Builder<ArcanusLootFunction.Builder> {
		@Override
		protected ArcanusLootFunction.Builder getThisBuilder() {
			return this;
		}

		@Override
		public LootFunction build() {
			return new ArcanusLootFunction(this.getConditions());
		}
	}

	public static class Serializer extends ConditionalLootFunction.Serializer<ArcanusLootFunction> {
		@Override
		public ArcanusLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
			return new ArcanusLootFunction(conditions);
		}
	}
}

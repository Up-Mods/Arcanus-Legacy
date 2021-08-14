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
		return switch(context.getRandom().nextInt(8)) {
			case 0 -> SpellBooks.getLungeBook(stack);
			case 1 -> SpellBooks.getDreamWarpBook(stack);
			case 2 -> SpellBooks.getMagicMissileBook(stack);
			case 3 -> SpellBooks.getTelekinesisBook(stack);
			case 4 -> SpellBooks.getHealBook(stack);
			case 5 -> SpellBooks.getDiscombobulateBook(stack);
			case 6 -> SpellBooks.getSolarStrikeBook(stack);
			case 7 -> SpellBooks.getArcaneWallBook(stack);
			default -> throw new IndexOutOfBoundsException("SOMETING WENT VEWY VEWY WWONG! THIWS SHOUWD NEVEW HAPPEN!");
		};
	}

	@Override
	public LootFunctionType getType() {
		return Arcanus.ARCANUS_LOOT_FUNCTION;
	}

	public static class Builder extends ConditionalLootFunction.Builder<ArcanusLootFunction.Builder> {
		@Override
		protected Builder getThisBuilder() {
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

package dev.cammiescorner.arcanus.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.actions.AltarAction;
import dev.cammiescorner.arcanus.api.actions.ItemAltarAction;
import dev.cammiescorner.arcanus.api.actions.SummonAltarAction;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import dev.cammiescorner.arcanus.common.registry.ArcanusRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AmethystAltarRecipe implements Recipe<AmethystAltarBlockEntity> {
	private final Identifier id;
	private final String group;
	private final DefaultedList<Ingredient> input;
	private final int power;
	private final AltarAction result;

	public AmethystAltarRecipe(Identifier id, String group, DefaultedList<Ingredient> input, int power, AltarAction result) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.power = power;
		this.result = result;
	}

	@Override
	public boolean matches(AmethystAltarBlockEntity altar, World world) {
		RecipeMatcher matcher = new RecipeMatcher();
		int i = 0;

		for(int j = 0; j < altar.size(); ++j) {
			ItemStack stack = altar.getStack(j);

			if(stack.isEmpty())
				continue;

			++i;
			matcher.addInput(stack, 1);
		}

		return i == input.size() && matcher.match(this, null);
	}

	@Override
	public ItemStack craft(AmethystAltarBlockEntity altar) {
		return ItemStack.EMPTY;
	}

	public void craft(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
		result.run(world, player, altar);
		altar.clear();
	}

	@Override
	public boolean fits(int width, int height) {
		return width <= 10 && width > 0 && height == 1;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public AltarAction getResult() {
		return result;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ArcanusRecipes.ALTAR_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return ArcanusRecipes.ALTAR_TYPE;
	}

	public int getPower() {
		return power;
	}

	public static class Serialiser implements RecipeSerializer<AmethystAltarRecipe> {
		@Override
		public AmethystAltarRecipe read(Identifier id, JsonObject json) {
			String group = JsonHelper.getString(json, "group", "");
			DefaultedList<Ingredient> ingredients = ShapelessRecipe.Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
			int power = JsonHelper.getInt(json, "power");
			JsonObject resultObj = JsonHelper.getObject(json, "result");
			String result = JsonHelper.getString(resultObj, "type");
			AltarAction action = Arcanus.ALTAR_ACTIONS.getOrEmpty(new Identifier(result)).orElseThrow(() -> new JsonSyntaxException("Expected result to be an altar action, was unknown string '" + result + "'"));

			if(action instanceof ItemAltarAction itemAction) {
				Item item = JsonHelper.getItem(resultObj, "item", Items.AIR);
				int count = JsonHelper.getInt(resultObj, "count", 1);
				itemAction.setStack(new ItemStack(item, count));
			}
			if(action instanceof SummonAltarAction summonAction) {
				String entityType = JsonHelper.getString(resultObj, "entity", "");
				summonAction.setEntityType(Registry.ENTITY_TYPE.get(new Identifier(entityType)));
			}

			if(ingredients.isEmpty())
				throw new JsonParseException("No ingredients for altar recipe");
			if(ingredients.size() > 10)
				throw new JsonParseException("Too many ingredients for altar recipe");

			return new AmethystAltarRecipe(id, group, ingredients, power, action);
		}

		@Override
		public AmethystAltarRecipe read(Identifier id, PacketByteBuf buf) {
			String group = buf.readString();
			int i = buf.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);

			for(int j = 0; j < ingredients.size(); ++j)
				ingredients.set(j, Ingredient.fromPacket(buf));

			int power = buf.readVarInt();
			AltarAction action = Arcanus.ALTAR_ACTIONS.get(buf.readVarInt());

			if(action instanceof ItemAltarAction item)
				item.setStack(buf.readItemStack());
			if(action instanceof SummonAltarAction summon)
				summon.setEntityType(Registry.ENTITY_TYPE.get(new Identifier(buf.readString())));

			return new AmethystAltarRecipe(id, group, ingredients, power, action);
		}

		@Override
		public void write(PacketByteBuf buf, AmethystAltarRecipe recipe) {
			buf.writeString(recipe.getGroup());
			buf.writeVarInt(recipe.getIngredients().size());

			for(Ingredient ingredient : recipe.getIngredients())
				ingredient.write(buf);

			buf.writeVarInt(recipe.getPower());
			buf.writeVarInt(Arcanus.ALTAR_ACTIONS.getRawId(recipe.getResult()));

			if(recipe.getResult() instanceof ItemAltarAction action)
				buf.writeItemStack(action.getStack());
			if(recipe.getResult() instanceof SummonAltarAction action)
				buf.writeString(Registry.ENTITY_TYPE.getId(action.getEntity().getType()).toString());
		}
	}
}

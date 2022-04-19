package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.recipes.AmethystAltarRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public class ArcanusRecipes {
	public static final RecipeType<AmethystAltarRecipe> ALTAR_TYPE = RecipeType.register(Arcanus.id("amethyst_altar").toString());
	public static final RecipeSerializer<AmethystAltarRecipe> ALTAR_SERIALIZER = RecipeSerializer.register(Arcanus.id("amethyst_altar").toString(), new AmethystAltarRecipe.Serialiser());

	public static void loadMeBitch() { }
}

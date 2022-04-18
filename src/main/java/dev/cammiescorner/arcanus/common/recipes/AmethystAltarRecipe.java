package dev.cammiescorner.arcanus.common.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class AmethystAltarRecipe implements CraftingRecipe {
	private final Identifier id;
	final String group;
	final ItemStack output;
	final DefaultedList<Ingredient> input;

	public AmethystAltarRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
		this.id = id;
		this.group = group;
		this.output = output;
		this.input = input;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory) {
		return null;
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
		return output;
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
		return null;
	}
}

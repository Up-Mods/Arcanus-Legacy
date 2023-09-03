package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.random.RandomGenerator;

public class SpellBooks {
	private static final int MAX_AUTHORS = 21;

	public static ItemStack getSpellBook(ItemStack stack, Spell spell, RandomGenerator random) {
		int randInt = random.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(Text.translatable("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(Text.translatable("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getSpellBook(Spell spell, RandomGenerator random) {
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);

		return SpellBooks.getSpellBook(stack, spell, random);
	}

	public static ItemStack getRandomSpellBook(ItemStack stack, RandomGenerator random) {
		return SpellBooks.getSpellBook(stack, Arcanus.SPELL.getRandom(random).orElseThrow().value(), random);
	}

	public static ItemStack getRandomSpellBook(RandomGenerator random) {
		return getRandomSpellBook(new ItemStack(Items.WRITTEN_BOOK), random);
	}

	private static NbtString textToNbt(Text text) {
		return NbtString.of(Text.Serializer.toJson(text));
	}

	private static String spellToPattern(Spell spell) {
		return spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol();
	}
}

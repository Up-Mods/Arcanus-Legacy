package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.registry.ModSpells;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Random;

public class SpellBooks {
	private static final Random RAND = new Random();
	private static final int MAX_AUTHORS = 20;

	public static ItemStack getLungeBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.LUNGE;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getDreamWarpBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.DREAM_WARP;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMagicMissileBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.MAGIC_MISSILE;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getTelekinesisBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.TELEKINESIS;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getHealBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.HEAL;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getDiscombobulateBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.DISCOMBOBULATE;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getSolarStrikeBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.SOLAR_STRIKE;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getArcaneWallBook(ItemStack stack) {
		int randInt = RAND.nextInt(MAX_AUTHORS);
		String number = randInt < 10 ? "0" + randInt : String.valueOf(randInt);
		Spell spell = ModSpells.ARCANE_WALL;
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtElement.STRING_TYPE);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + number + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(textToNbt(new TranslatableText("book." + Arcanus.SPELL.getId(spell).getNamespace() + "." + Arcanus.SPELL.getId(spell).getPath() + ".description")
				.append(new TranslatableText("book." + Arcanus.MOD_ID + ".casting_pattern"))
				.append("          " + spellToPattern(spell))));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getBookFromSpell(Spell spell) {
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);

		return switch(Arcanus.SPELL.getRawId(spell)) {
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

	public static ItemStack getRandomSpellBook(ItemStack stack) {
		return switch(RAND.nextInt(7)) {
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

	public static ItemStack getRandomSpellBook() {
		return getRandomSpellBook(new ItemStack(Items.WRITTEN_BOOK));
	}

	private static NbtString textToNbt(Text text) {
		return NbtString.of(Text.Serializer.toJson(text));
	}

	private static String spellToPattern(Spell spell) {
		return spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol();
	}
}

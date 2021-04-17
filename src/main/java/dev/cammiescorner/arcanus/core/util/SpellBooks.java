package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;

import java.util.Random;

public class SpellBooks
{
	private static Random random = new Random();

	public static ItemStack getLungeBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:lunge"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getFissureBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:fissure"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMagicMissileBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:magic_missile"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getVanishBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:vanish"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getHealBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:heal"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMeteorBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:meteor"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getIceSpireBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:ice_spire"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMineBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:mine"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(StringTag.of(Arcanus.getSpellInputs(spell.getSpellPattern(), 0) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 1) + "-" + Arcanus.getSpellInputs(spell.getSpellPattern(), 2)));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getBookFromSpell(Spell spell)
	{
		switch(Arcanus.SPELL.getRawId(spell))
		{
			case 0:
				return SpellBooks.getLungeBook().copy();
			case 1:
				return SpellBooks.getFissureBook().copy();
			case 2:
				return SpellBooks.getMagicMissileBook().copy();
			case 3:
				return SpellBooks.getVanishBook().copy();
			case 4:
				return SpellBooks.getHealBook().copy();
			case 5:
				return SpellBooks.getMeteorBook().copy();
			case 6:
				return SpellBooks.getIceSpireBook().copy();
			case 7:
				return SpellBooks.getMineBook().copy();
		}

		return new ItemStack(Items.AIR);
	}
}

package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.Random;

public class SpellBooks
{
	private static final Random RAND = new Random();

	public static ItemStack getLungeBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:lunge"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getFissureBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:fissure"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMagicMissileBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:magic_missile"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getVanishBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:vanish"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getHealBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:heal"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMeteorBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:meteor"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getIceSpireBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:ice_spire"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMineBook()
	{
		Spell spell = Arcanus.SPELL.get(new Identifier("arcanus:mine"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		NbtCompound tag = stack.getOrCreateNbt();
		NbtList listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getId(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + "." + RAND.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getId(spell).toString());
		listTag.add(NbtString.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getBookFromSpell(Spell spell)
	{
		switch(Arcanus.SPELL.getRawId(spell))
		{
			case 0:
				return SpellBooks.getLungeBook();
			case 1:
				return SpellBooks.getFissureBook();
			case 2:
				return SpellBooks.getMagicMissileBook();
			case 3:
				return SpellBooks.getVanishBook();
			case 4:
				return SpellBooks.getHealBook();
			case 5:
				return SpellBooks.getMeteorBook();
			case 6:
				return SpellBooks.getIceSpireBook();
			case 7:
				return SpellBooks.getMineBook();
		}

		return new ItemStack(Items.AIR);
	}
}

package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import java.util.Random;

public class SpellBooks
{
	private static Random random = new Random();

	public static ItemStack getLungeBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:lunge"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getFissureBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:fissure"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMagicMissileBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:magic_missile"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getVanishBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:vanish"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getHealBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:heal"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMeteorBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:meteor"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getIceSpireBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:ice_spire"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getMineBook()
	{
		Spell spell = Arcanus.SPELL.get(new ResourceLocation("arcanus:mine"));
		ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tag = stack.getOrCreateTag();
		ListTag listTag = tag.getList("pages", NbtType.STRING);

		tag.putString("title", "book." + Arcanus.SPELL.getKey(spell).toString().replace(':', '.') + ".title");
		tag.putString("author", "book." + Arcanus.MOD_ID + random.nextInt(16) + ".author");
		tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
		listTag.add(StringTag.of(spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol()));
		tag.put("pages", listTag);

		return stack;
	}

	public static ItemStack getBookFromSpell(Spell spell)
	{
		switch(Arcanus.SPELL.getId(spell))
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

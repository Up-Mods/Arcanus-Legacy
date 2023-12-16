package dev.cammiescorner.arcanus.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class SpellBooks {

    /**
     * value used in recipe outputs to create a spell book
     */
    public static final String RECIPE_IDENTIFIER = Arcanus.id("spell_book").toString();
    private static final int MAX_AUTHORS = 21;

    public static ItemStack getSpellBook(ItemStack stack, Spell spell, RandomSource random) {
        return getSpellBookWithAuthor(stack, spell, getAuthorKey(random.nextInt(MAX_AUTHORS)));
    }

    public static String getAuthorKey(int index) {
        return Arcanus.translationKey("book", String.format("%02d", index), "author");
    }

    public static ItemStack getSpellBookWithAuthor(ItemStack stack, Spell spell, @Nullable String authorKey) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag listTag = tag.getList("pages", Tag.TAG_STRING);

        tag.putString("title", Arcanus.SPELL.getKey(spell).toLanguageKey("book", "title").replace('/', '.'));
        if (authorKey != null)
            tag.putString("author", authorKey);
        tag.putString("spell", Arcanus.SPELL.getKey(spell).toString());
        listTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.translatable(Arcanus.SPELL.getKey(spell).toLanguageKey("book", "description").replace('/', '.'))
                .append(Arcanus.translate("book", "casting_pattern"))
                .append("          " + spellToPattern(spell)))));
        tag.put("pages", listTag);

        return stack;
    }

    public static ItemStack getSpellBook(Spell spell, RandomSource random) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);

        return SpellBooks.getSpellBook(stack, spell, random);
    }

    public static ItemStack getSpellBookWithAuthor(Spell spell, String authorKey) {
        ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);

        return SpellBooks.getSpellBookWithAuthor(stack, spell, authorKey);
    }

    public static ItemStack getRandomSpellBook(ItemStack stack, RandomSource random) {
        return SpellBooks.getSpellBook(stack, Arcanus.SPELL.getRandom(random).orElseThrow().value(), random);
    }

    public static ItemStack getRandomSpellBook(RandomSource random) {
        return getRandomSpellBook(new ItemStack(Items.WRITTEN_BOOK), random);
    }

    private static String spellToPattern(Spell spell) {
        return spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol();
    }
}

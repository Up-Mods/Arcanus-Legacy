package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.item.ManaFlaskItem;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ArcanusItems {

    public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final Item MASTER_WAND = create("master_wand", new WandItem(-0.5F, 6400, null, new Item.Settings()));
    public static final Item ADEPT_WAND = create("adept_wand", new WandItem(0F, 6400, () -> ArcanusItems.MASTER_WAND, new Item.Settings()));
    public static final Item INITIATE_WAND = create("initiate_wand", new WandItem(0.5F, 2400, () -> ArcanusItems.ADEPT_WAND, new Item.Settings()));
    public static final Item MANA_FLASK = create("mana_flask", new ManaFlaskItem());

    public static void register() {
        FabricItemGroup.builder(Arcanus.id("general")).icon(() -> new ItemStack(ArcanusItems.MASTER_WAND)).entries((displayParams, entries) -> {
            entries.addItem(ArcanusBlocks.BOOKSHELF);
            entries.addItem(ArcanusBlocks.DISPLAY_CASE);
            ArcanusHelper.addWandsToTab(entries);
            ItemStack stack = new ItemStack(MANA_FLASK);
            NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
            tag.putInt("Mana", 0);
            entries.addStack(stack);
            stack = new ItemStack(MANA_FLASK);
            tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
            tag.putInt("Mana", 4);
            entries.addStack(stack);
            Arcanus.SPELL.forEach(spell -> entries.addStack(SpellBooks.getSpellBook(spell, MinecraftClient.getInstance().player.getRandom())));
        }).build();

        ITEMS.forEach((item, id) -> Registry.register(Registries.ITEM, id, item));
    }

    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Arcanus.id(name));
        return item;
    }
}

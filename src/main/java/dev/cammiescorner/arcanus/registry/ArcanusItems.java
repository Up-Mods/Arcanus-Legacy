package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.item.ManaFlaskItem;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.LinkedHashMap;

public class ArcanusItems {

    public static final LinkedHashMap<Item, ResourceLocation> ITEMS = new LinkedHashMap<>();

    public static final Item MASTER_WAND = create("master_wand", new WandItem(-0.5F, 6400, null, new Item.Properties()));
    public static final Item ADEPT_WAND = create("adept_wand", new WandItem(0F, 6400, () -> ArcanusItems.MASTER_WAND, new Item.Properties()));
    public static final Item INITIATE_WAND = create("initiate_wand", new WandItem(0.5F, 2400, () -> ArcanusItems.ADEPT_WAND, new Item.Properties()));
    public static final Item MANA_FLASK = create("mana_flask", new ManaFlaskItem());

    public static void register() {
        FabricItemGroup.builder(Arcanus.id("general")).icon(() -> new ItemStack(ArcanusItems.MASTER_WAND)).displayItems((displayParams, entries) -> {
            entries.accept(ArcanusBlocks.BOOKSHELF);
            entries.accept(ArcanusBlocks.DISPLAY_CASE);
            ArcanusHelper.addWandsToTab(entries);
            ItemStack stack = new ItemStack(MANA_FLASK);
            CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
            tag.putInt("Mana", 0);
            entries.accept(stack);
            stack = new ItemStack(MANA_FLASK);
            tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
            tag.putInt("Mana", 4);
            entries.accept(stack);
            Arcanus.SPELL.forEach(spell -> entries.accept(SpellBooks.getSpellBook(spell, Minecraft.getInstance().player.getRandom())));
        }).build();

        ITEMS.forEach((item, id) -> Registry.register(BuiltInRegistries.ITEM, id, item));
    }

    private static <T extends Item> T create(String name, T item) {
        ITEMS.put(item, Arcanus.id(name));
        return item;
    }
}

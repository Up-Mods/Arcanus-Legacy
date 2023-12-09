package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.item.ManaFlaskItem;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.SpellBooks;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ArcanusItems {

    public static final RegistryHandler<CreativeModeTab> CREATIVE_TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, Arcanus.MODID);
    public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Arcanus.MODID);

    public static final RegistrySupplier<Item> MASTER_WAND = ITEMS.register("master_wand", () -> new WandItem(-0.5F, 6400, null, new Item.Properties()));
    public static final RegistrySupplier<Item> ADEPT_WAND = ITEMS.register("adept_wand", () -> new WandItem(0F, 6400, ArcanusItems.MASTER_WAND, new Item.Properties()));
    public static final RegistrySupplier<Item> INITIATE_WAND = ITEMS.register("initiate_wand", () -> new WandItem(0.5F, 2400, ArcanusItems.ADEPT_WAND, new Item.Properties()));
    public static final RegistrySupplier<Item> MANA_FLASK = ITEMS.register("mana_flask", () -> new ManaFlaskItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().alwaysEat().build())));

    public static final RegistrySupplier<CreativeModeTab> ITEM_GROUP = CREATIVE_TABS.register("general", () -> FabricItemGroup.builder()
            .title(Arcanus.translate("itemGroup", "general"))
            .icon(() -> new ItemStack(ArcanusItems.MASTER_WAND.get()))
            .displayItems((displayParams, entries) -> {
                entries.accept(ArcanusBlocks.BOOKSHELF.get());
                entries.accept(ArcanusBlocks.DISPLAY_CASE.get());
                ArcanusHelper.addWandsToTab(entries);
                ItemStack stack = new ItemStack(MANA_FLASK.get());
                CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MODID);
                tag.putInt("Mana", 0);
                entries.accept(stack);
                stack = new ItemStack(MANA_FLASK.get());
                tag = stack.getOrCreateTagElement(Arcanus.MODID);
                tag.putInt("Mana", 4);
                entries.accept(stack);
                Arcanus.SPELL.forEach(spell -> entries.accept(SpellBooks.getSpellBook(spell, Minecraft.getInstance().player.getRandom())));
            }).build());
}

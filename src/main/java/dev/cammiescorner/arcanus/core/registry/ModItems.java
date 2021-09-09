package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.ManaFlaskItem;
import dev.cammiescorner.arcanus.common.items.WandItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModItems {
	//-----Item Map-----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item INITIATE_WAND = create("initiate_wand", new WandItem(0.5F, 3200, () -> ModItems.ADEPT_WAND));
	public static final Item ADEPT_WAND = create("adept_wand", new WandItem(0F, 6400,  () -> ModItems.MASTER_WAND));
	public static final Item MASTER_WAND = create("master_wand", new WandItem(-0.5F, 6400, null));
	public static final Item MANA_FLASK = create("mana_flask", new ManaFlaskItem());

	//-----Registry-----//
	public static void register() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Arcanus.MOD_ID, name));
		return item;
	}
}

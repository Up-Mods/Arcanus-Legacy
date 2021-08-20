package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.ManaFlaskItem;
import dev.cammiescorner.arcanus.common.items.WandItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModItems {
	//-----Item Map-----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item WAND = create("wand", new WandItem(new FabricItemSettings().maxCount(1).group(Arcanus.ITEM_GROUP)));
	public static final Item MANA_FLASK = create("mana_flask", new ManaFlaskItem(new FabricItemSettings().maxCount(1).group(Arcanus.ITEM_GROUP)));

	//-----Registry-----//
	public static void register() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Arcanus.MOD_ID, name));
		return item;
	}
}

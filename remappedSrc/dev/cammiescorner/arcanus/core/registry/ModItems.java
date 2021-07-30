package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.WandItem;
import java.util.LinkedHashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems
{
	//-----Item Map-----//
	public static final LinkedHashMap<Item, ResourceLocation> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item WAND = create("wand", new WandItem(new Item.Properties().stacksTo(1).tab(Arcanus.ITEM_GROUP)));

	//-----Registry-----//
	public static void register()
	{
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item)
	{
		ITEMS.put(item, new ResourceLocation(Arcanus.MOD_ID, name));
		return item;
	}
}

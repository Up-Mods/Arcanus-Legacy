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
	public static final Item INITIATE_WAND = create("initiate_wand", new WandItem(0.5F, 2400, () -> ModItems.ADEPT_WAND));
	public static final Item ADEPT_WAND = create("adept_wand", new WandItem(0F, 6400,  () -> ModItems.MASTER_WAND));
	public static final Item MASTER_WAND = create("master_wand", new WandItem(-0.5F, 6400, null));
	public static final Item MANA_FLASK = create("mana_flask", new ManaFlaskItem());

//	//-----Rings-----//
//	public static final Item RING_OF_SHIELDING = create("ring_of_shielding", new RingOfShieldingTrinket());
//	public static final Item RING_OF_THE_ENDERMAN = create("ring_of_the_enderman", new RingOfTheEndermanTrinket());
//	public static final Item RING_OF_THE_MONK = create("ring_of_the_monk", new RingOfTheMonkTrinket());
//	public static final Item RING_OF_THE_WEST_WIND = create("ring_of_the_west_wind", new RingOfTheWestWindTrinket());
//
//	//-----Amulets-----//
//	public static final Item AMULET_OF_SOOTHING = create("amulet_of_soothing", new AmuletOfSoothingTrinket());
//	public static final Item AMULET_OF_PURGING = create("amulet_of_purging", new AmuletOfPurgingTrinket());
//	public static final Item AMULET_OF_THE_MASTER = create("amulet_of_the_master", new AmuletOfTheMasterTrinket());
//	// ???
//
//	//-----Belts-----//
//	public static final Item BELT_OF_THE_RABBIT = create("belt_of_the_rabbit", new BeltOfTheRabbitTrinket());
//	public static final Item BELT_OF_REJUVENATION = create("belt_of_rejuvenation", new BeltOfRejuvenationTrinket());
//	// ???
//	// ???
//
//	//-----Gloves-----//
//	public static final Item GLOVES_OF_DISENGAGEMENT = create("gloves_of_disengagement", new GlovesOfDisengagementTrinket());
//	public static final Item GLOVES_OF_THE_HIPPOCRATIC_OATH = create("gloves_of_the_hippocratic_oath", new GlovesOfTheHippocraticOathTrinket());
//	// ???
//	// ???

	//-----Registry-----//
	public static void register() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, new Identifier(Arcanus.MOD_ID, name));
		return item;
	}
}

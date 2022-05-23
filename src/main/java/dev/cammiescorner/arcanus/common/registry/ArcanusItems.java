package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.common.items.FaeStoneItem;
import dev.cammiescorner.arcanus.common.items.MageRobesItem;
import dev.cammiescorner.arcanus.common.items.TimeCultistRobesItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ArcanusItems {
	//-----Item Map-----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item ROUGH_FAE_STONE = create("rough_fae_stone", new FaeStoneItem());
	public static final Item PASSABLE_FAE_STONE = create("passable_fae_stone", new FaeStoneItem());
	public static final Item MAGE_HOOD = create("mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.NONE));
	public static final Item MAGE_ROBE = create("mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.NONE));
	public static final Item MAGE_BELT = create("mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.NONE));
	public static final Item MAGE_BOOTS = create("mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.NONE));
	public static final Item ENHANCER_MAGE_HOOD = create("enhancer_mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.ENHANCER));
	public static final Item ENHANCER_MAGE_ROBE = create("enhancer_mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.ENHANCER));
	public static final Item ENHANCER_MAGE_BELT = create("enhancer_mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.ENHANCER));
	public static final Item ENHANCER_MAGE_BOOTS = create("enhancer_mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.ENHANCER));
	public static final Item TRANSMUTER_MAGE_HOOD = create("transmuter_mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.TRANSMUTER));
	public static final Item TRANSMUTER_MAGE_ROBE = create("transmuter_mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.TRANSMUTER));
	public static final Item TRANSMUTER_MAGE_BELT = create("transmuter_mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.TRANSMUTER));
	public static final Item TRANSMUTER_MAGE_BOOTS = create("transmuter_mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.TRANSMUTER));
	public static final Item EMITTER_MAGE_HOOD = create("emitter_mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.EMITTER));
	public static final Item EMITTER_MAGE_ROBE = create("emitter_mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.EMITTER));
	public static final Item EMITTER_MAGE_BELT = create("emitter_mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.EMITTER));
	public static final Item EMITTER_MAGE_BOOTS = create("emitter_mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.EMITTER));
	public static final Item CONJURER_MAGE_HOOD = create("conjurer_mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.CONJURER));
	public static final Item CONJURER_MAGE_ROBE = create("conjurer_mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.CONJURER));
	public static final Item CONJURER_MAGE_BELT = create("conjurer_mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.CONJURER));
	public static final Item CONJURER_MAGE_BOOTS = create("conjurer_mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.CONJURER));
	public static final Item MANIPULATOR_MAGE_HOOD = create("manipulator_mage_hood", new MageRobesItem(EquipmentSlot.HEAD, AuraType.MANIPULATOR));
	public static final Item MANIPULATOR_MAGE_ROBE = create("manipulator_mage_robe", new MageRobesItem(EquipmentSlot.CHEST, AuraType.MANIPULATOR));
	public static final Item MANIPULATOR_MAGE_BELT = create("manipulator_mage_belt", new MageRobesItem(EquipmentSlot.LEGS, AuraType.MANIPULATOR));
	public static final Item MANIPULATOR_MAGE_BOOTS = create("manipulator_mage_boots", new MageRobesItem(EquipmentSlot.FEET, AuraType.MANIPULATOR));

	public static final Item TIME_CULTIST_HOOD = create("time_cultist_hood", new TimeCultistRobesItem(EquipmentSlot.HEAD));
	public static final Item TIME_CULTIST_ROBE = create("time_cultist_robe", new TimeCultistRobesItem(EquipmentSlot.CHEST));
	public static final Item TIME_CULTIST_LEGGINGS = create("time_cultist_leggings", new TimeCultistRobesItem(EquipmentSlot.LEGS));
	public static final Item TIME_CULTIST_BOOTS = create("time_cultist_boots", new TimeCultistRobesItem(EquipmentSlot.FEET));

	//-----Registry-----//
	public static void register() {
		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, Arcanus.id(name));
		return item;
	}
}

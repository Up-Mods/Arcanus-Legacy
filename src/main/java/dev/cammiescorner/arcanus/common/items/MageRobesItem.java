package dev.cammiescorner.arcanus.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.common.registry.ArcanusMaterials;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.UUID;

public class MageRobesItem extends ArmorItem {
	private static final UUID[] MODIFIERS = new UUID[]{
			UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
			UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
			UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
			UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
	};
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public MageRobesItem(EquipmentSlot slot, AuraType type) {
		super(ArcanusMaterials.Armour.MAGE_ROBES, slot, new QuiltItemSettings().group(Arcanus.ITEM_GROUP));
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		UUID uuid = MODIFIERS[slot.getEntitySlotId()];

		builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "Armor modifier", ArcanusMaterials.Armour.MAGE_ROBES.getProtectionAmount(slot), EntityAttributeModifier.Operation.ADDITION));

		switch(type) {
			case ENHANCER ->
				builder.put(ArcanusAttributes.ENHANCEMENT_AFFINITY, new EntityAttributeModifier(uuid, "Enhancer modifier", 0.1, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			case TRANSMUTER ->
				builder.put(ArcanusAttributes.TRANSMUTATION_AFFINITY, new EntityAttributeModifier(uuid, "Transmuter modifier", 0.1, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			case EMITTER ->
				builder.put(ArcanusAttributes.EMISSION_AFFINITY, new EntityAttributeModifier(uuid, "Emitter modifier", 0.1, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			case CONJURER ->
				builder.put(ArcanusAttributes.CONJURATION_AFFINITY, new EntityAttributeModifier(uuid, "Conjurer modifier", 0.1, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			case MANIPULATOR ->
				builder.put(ArcanusAttributes.MANIPULATION_AFFINITY, new EntityAttributeModifier(uuid, "Manipulator modifier", 0.1, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			default -> {
				builder.put(ArcanusAttributes.ENHANCEMENT_AFFINITY, new EntityAttributeModifier(uuid, "Enhancer modifier", 0.04, EntityAttributeModifier.Operation.MULTIPLY_BASE));
				builder.put(ArcanusAttributes.TRANSMUTATION_AFFINITY, new EntityAttributeModifier(uuid, "Transmuter modifier", 0.04, EntityAttributeModifier.Operation.MULTIPLY_BASE));
				builder.put(ArcanusAttributes.EMISSION_AFFINITY, new EntityAttributeModifier(uuid, "Emitter modifier", 0.04, EntityAttributeModifier.Operation.MULTIPLY_BASE));
				builder.put(ArcanusAttributes.CONJURATION_AFFINITY, new EntityAttributeModifier(uuid, "Conjurer modifier", 0.04, EntityAttributeModifier.Operation.MULTIPLY_BASE));
				builder.put(ArcanusAttributes.MANIPULATION_AFFINITY, new EntityAttributeModifier(uuid, "Manipulator modifier", 0.04, EntityAttributeModifier.Operation.MULTIPLY_BASE));
			}
		}

		attributeModifiers = builder.build();
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == this.slot ? attributeModifiers : super.getAttributeModifiers(slot);
	}
}

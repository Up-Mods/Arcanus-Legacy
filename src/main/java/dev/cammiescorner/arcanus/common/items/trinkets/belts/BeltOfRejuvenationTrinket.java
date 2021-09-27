package dev.cammiescorner.arcanus.common.items.trinkets.belts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.trinkets.ArcanusTrinketItem;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class BeltOfRejuvenationTrinket extends ArcanusTrinketItem {
	private static final UUID MANA_REGEN_UUID = UUID.fromString("10158082-201d-4cdf-87d8-aeed46227ad6");
	private static final UUID BURNOUT_REGEN_UUID = UUID.fromString("5ca55efd-61ba-471c-a6d5-00751fb520cc");
	private static final UUID MANA_LOCK_UUID = UUID.fromString("4ae0d063-4293-43ce-81ea-9bc18a3eaf8c");

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Arcanus.EntityAttributes.MANA_REGEN, new EntityAttributeModifier(MANA_REGEN_UUID, "Mana regen modifier", -0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		builder.put(Arcanus.EntityAttributes.BURNOUT_REGEN, new EntityAttributeModifier(BURNOUT_REGEN_UUID, "Burnout regen modifier", -0.67, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		builder.put(Arcanus.EntityAttributes.MANA_LOCK, new EntityAttributeModifier(MANA_LOCK_UUID, "Mana lock modifier", 7, EntityAttributeModifier.Operation.ADDITION));

		return builder.build();
	}
}

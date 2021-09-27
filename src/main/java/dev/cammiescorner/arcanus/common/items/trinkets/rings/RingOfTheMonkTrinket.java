package dev.cammiescorner.arcanus.common.items.trinkets.rings;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.common.items.trinkets.ArcanusTrinketItem;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class RingOfTheMonkTrinket extends ArcanusTrinketItem {
	private static final UUID DAMAGE_UUID = UUID.fromString("3a47692b-983c-43a2-8f35-c53452e36470");
	private static final float DAMAGE_AMOUNT = 5;

	@Override
	public float onAttack(DamageSource source, float amount, LivingEntity target, LivingEntity attacker) {
		if(attacker instanceof MagicUser user) {
			boolean doNormalDamage = attacker.getMainHandStack().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE) && user.getMana() > 0;

			if(doNormalDamage)
				return amount - DAMAGE_AMOUNT;

			if(target.canTakeDamage() && target.hurtTime == 0) {
				user.setLastCastTime(attacker.world.getTime());
				user.addMana(-1);
			}
		}

		return amount;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(DAMAGE_UUID, "Damage modifier", DAMAGE_AMOUNT, EntityAttributeModifier.Operation.ADDITION));

		return builder.build();
	}
}

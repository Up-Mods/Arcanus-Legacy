package dev.cammiescorner.arcanus.common.items.trinkets.rings;

import dev.cammiescorner.arcanus.common.items.trinkets.ArcanusTrinketItem;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class RingOfShieldingTrinket extends ArcanusTrinketItem {
	@Override
	public float onDamaged(DamageSource source, float amount, LivingEntity target, LivingEntity attacker) {
		if(target instanceof MagicUser user && user.getMana() > 0) {
			user.setLastCastTime(attacker.world.getTime());

			if(user.getMana() >= amount) {
				user.addMana((int) -amount);
				return 0.001F;
			}
			else {
				user.addMana((int) -amount);
				return amount - user.getMana();
			}
		}

		return super.onDamaged(source, amount, target, attacker);
	}
}

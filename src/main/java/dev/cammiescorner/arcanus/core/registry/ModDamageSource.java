package dev.cammiescorner.arcanus.core.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class ModDamageSource extends DamageSource {
	public static final DamageSource MAGIC_BURNOUT = new ModDamageSource("magic_burnout").setBypassesArmor().setUnblockable();

	protected ModDamageSource(String name) {
		super(name);
	}

	public static DamageSource solarStrike(Entity attacker) {
		return new EntityDamageSource("solar_strike", attacker).setUsesMagic();
	}
}

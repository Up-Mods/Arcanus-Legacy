package dev.cammiescorner.arcanus.core.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ModDamageSource extends DamageSource {
	public static final DamageSource MAGIC_BURNOUT = new ModDamageSource("magic_burnout").setBypassesArmor().setUnblockable();

	protected ModDamageSource(String name) {
		super(name);
	}

	public static DamageSource solarStrike(Entity attacker) {
		return new EntityDamageSource("solar_strike", attacker) {
			@Override
			public Text getDeathMessage(LivingEntity entity) {
				Text text = attacker == null ? Text.translatable("entity.arcanus.solar_strike") : attacker.getDisplayName();
				ItemStack itemStack = attacker instanceof LivingEntity living ? living.getMainHandStack() : ItemStack.EMPTY;
				String string = "death.attack." + this.name;
				String string2 = string + ".item";
				return !itemStack.isEmpty() && itemStack.hasCustomName() ? Text.translatable(string2, entity.getDisplayName(), text, itemStack.toHoverableText()) : Text.translatable(string, entity.getDisplayName(), text);
			}
		}.setUsesMagic();
	}
}

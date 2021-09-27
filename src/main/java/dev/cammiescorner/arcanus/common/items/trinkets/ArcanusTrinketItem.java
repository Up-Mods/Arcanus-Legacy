package dev.cammiescorner.arcanus.common.items.trinkets;

import dev.cammiescorner.arcanus.Arcanus;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public abstract class ArcanusTrinketItem extends TrinketItem {
	public ArcanusTrinketItem() {
		super(new FabricItemSettings().group(Arcanus.ITEM_GROUP).maxCount(1));
	}

	public float onAttack(DamageSource source, float amount, LivingEntity target, LivingEntity attacker) {
		return amount;
	}

	public float onDamaged(DamageSource source, float amount, LivingEntity target, LivingEntity attacker) {
		return amount;
	}
}

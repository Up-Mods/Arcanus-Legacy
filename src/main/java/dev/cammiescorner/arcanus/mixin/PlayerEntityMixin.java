package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void arcanus$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(Arcanus.EntityAttributes.AURA_COST).add(Arcanus.EntityAttributes.AURA_REGEN).add(Arcanus.EntityAttributes.AURA_LOCK);
	}
}

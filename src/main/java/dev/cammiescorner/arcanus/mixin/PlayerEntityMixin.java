package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void arcanus$createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(ArcanusAttributes.AURA_COST).add(ArcanusAttributes.AURA_REGEN)
				.add(ArcanusAttributes.AURA_LOCK).add(ArcanusAttributes.ENHANCEMENT_AFFINITY)
				.add(ArcanusAttributes.TRANSMUTATION_AFFINITY).add(ArcanusAttributes.EMISSION_AFFINITY)
				.add(ArcanusAttributes.CONJURATION_AFFINITY).add(ArcanusAttributes.MANIPULATION_AFFINITY);
	}
	@Inject(method = "tick", at = @At("TAIL"))
	private void arcanus$dumbassTick(CallbackInfo ci) {
		//https://open.spotify.com/track/3NcsoNb0lwn0NJwUU7BjV5?si=815c00a6e2d44d79
		this.bodyYaw = MathHelper.clamp(this.bodyYaw, this.headYaw -16, this.headYaw + 16);
	}
}

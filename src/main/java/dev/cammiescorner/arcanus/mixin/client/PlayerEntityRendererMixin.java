package dev.cammiescorner.arcanus.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.RobesItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 0
	))
	private boolean arcanus$hideHat(boolean original, AbstractClientPlayerEntity player) {
		ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
		NbtCompound nbt = stack.getSubNbt(Arcanus.MOD_ID);
		boolean isClosed = nbt != null && nbt.getBoolean("Closed");

		if(stack.getItem() instanceof RobesItem && isClosed)
			return false;

		return original;
	}

	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 2
	))
	private boolean arcanus$hideLeftPants(boolean original, AbstractClientPlayerEntity player) {
		if(player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof RobesItem)
			return false;

		return original;
	}

	@ModifyExpressionValue(method = "setModelPose", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isPartVisible(Lnet/minecraft/client/render/entity/PlayerModelPart;)Z",
			ordinal = 3
	))
	private boolean arcanus$hideRightPants(boolean original, AbstractClientPlayerEntity player) {
		if(player.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof RobesItem)
			return false;

		return original;
	}
}

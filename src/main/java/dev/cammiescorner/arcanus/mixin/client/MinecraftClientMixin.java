package dev.cammiescorner.arcanus.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.packets.c2s.CastSpellPacket;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Nullable public ClientPlayerEntity player;
	@Shadow @Nullable public HitResult crosshairTarget;

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"
	))
	public void arcanus$castSpells(CallbackInfo info) {
		if(player != null) {
			boolean bl = true;

			if(crosshairTarget instanceof BlockHitResult hitResult) {
				BlockState state = player.world.getBlockState(hitResult.getBlockPos());
				ActionResult result = state.onUse(player.world, player, player.preferredHand, hitResult);

				bl = !result.isAccepted();
			}

			if(bl && ArcanusHelper.canCastSpell(player, ArcanusHelper.getSelectedSpell(player))) {
				CastSpellPacket.send();
				player.swingHand(Hand.MAIN_HAND);
			}
		}
	}

	@ModifyExpressionValue(method = "doItemUse", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isEmpty()Z",
			ordinal = 1
	))
	public boolean arcanus$noRightClick(boolean original) {
		return original || !(ArcanusHelper.isCasting(player) && ArcanusHelper.canCastSpell(player, ArcanusHelper.getSelectedSpell(player)));
	}
}

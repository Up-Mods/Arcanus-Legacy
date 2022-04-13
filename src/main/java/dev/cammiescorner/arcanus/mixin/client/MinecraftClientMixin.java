package dev.cammiescorner.arcanus.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.packets.c2s.CastSpellPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Nullable public ClientPlayerEntity player;

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"
	))
	public void castSpells(CallbackInfo info) {
		if(player != null && ArcanusHelper.canCastSpell(player, ArcanusHelper.getSelectedSpell(player))) {
			CastSpellPacket.send();
			player.swingHand(Hand.MAIN_HAND);
		}
	}

	@WrapWithCondition(method = "handleInputEvents", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V"
	))
	public boolean noRightClick(MinecraftClient instance) {
		return !ArcanusHelper.isCasting(player);
	}

	@WrapWithCondition(method = "handleInputEvents", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/MinecraftClient;doItemPick()V"
	))
	public boolean noMiddleClick(MinecraftClient instance) {
		return !ArcanusHelper.isCasting(player);
	}
}

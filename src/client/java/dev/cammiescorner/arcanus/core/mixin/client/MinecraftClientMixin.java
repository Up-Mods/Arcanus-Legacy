package dev.cammiescorner.arcanus.core.mixin.client;

import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.common.items.WandItem;
import dev.cammiescorner.arcanus.core.util.ClientUtils;
import dev.cammiescorner.arcanus.core.util.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements ClientUtils {
	@Shadow	@Nullable public ClientPlayerEntity player;

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V", ordinal = 0), cancellable = true)
	public void onRightClick(CallbackInfo info) {
		if(player != null && !player.isSpectator() && player.getMainHandStack().getItem() instanceof WandItem) {
			ArcanusClient.timer = 20;
			ArcanusClient.unfinishedSpell = true;
			ArcanusClient.pattern.add(Pattern.RIGHT);
			player.swingHand(Hand.MAIN_HAND);
			player.world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1.1F);
			info.cancel();
		}
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", ordinal = 0), cancellable = true)
	public void onLeftClick(CallbackInfo info) {
		if(player != null && !player.isSpectator() && player.getMainHandStack().getItem() instanceof WandItem) {
			ArcanusClient.timer = 20;
			ArcanusClient.unfinishedSpell = true;
			ArcanusClient.pattern.add(Pattern.LEFT);
			player.swingHand(Hand.MAIN_HAND);
			player.world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1F, 1.3F);
			info.cancel();
		}
	}

	@Override
	public List<Pattern> getPattern() {
		return ArcanusClient.pattern;
	}

	@Override
	public void setTimer(int value) {
		ArcanusClient.timer = value;
	}

	@Override
	public void setUnfinishedSpell(boolean value) {
		ArcanusClient.unfinishedSpell = value;
	}
}

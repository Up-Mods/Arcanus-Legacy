package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	@Shadow protected abstract PlayerEntity getCameraPlayer();
	@Shadow private int scaledHeight;
	@Shadow private int scaledWidth;

	@Inject(method = "renderHotbar", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
			ordinal = 1
	))
	public void funkyBusiness(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		int i = scaledWidth / 2;
		Arm arm = getCameraPlayer().getMainArm();

		if(ArcanusHelper.isCasting(getCameraPlayer())) {
			if(arm == Arm.LEFT) {
				drawTexture(matrices, i - 121, scaledHeight - 23, 24, 22, 29, 24);
				drawTexture(matrices, i - 121, scaledHeight - 23, 0, 22, 24, 22);
			}
			else {
				drawTexture(matrices, i + 98, scaledHeight - 23, 24, 22, 29, 24);
				drawTexture(matrices, i + 98, scaledHeight - 23, 0, 22, 24, 22);
			}
		}
	}
}

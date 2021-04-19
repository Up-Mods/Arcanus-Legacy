package dev.cammiescorner.arcanus.core.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@Inject(method = "getHeartRows", at = @At("RETURN"), cancellable = true)
	public void getHeartRows(int heartCount, CallbackInfoReturnable<Integer> info)
	{
		info.setReturnValue(info.getReturnValueI() + 1);
	}
}

package dev.cammiescorner.arcanus.core.mixin.client;

import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "getHeartRows", at = @At("RETURN"), cancellable = true)
    public void getHeartRows(int heartCount, CallbackInfoReturnable<Integer> info) {
        if (client.player instanceof MagicUser user && user.isManaVisible())
            info.setReturnValue(info.getReturnValueI() + 1);
    }
}

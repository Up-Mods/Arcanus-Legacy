package dev.cammiescorner.arcanus.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.arcanus.entity.MagicUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyReturnValue(method = "getHeartRows", at = @At("RETURN"))
    public int getHeartRows(int original) {
        if (client.player instanceof MagicUser user && user.isManaVisible())
            return original + 1;
        return original;
    }
}

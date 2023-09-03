package dev.cammiescorner.arcanus.core.mixin.client;

import dev.cammiescorner.arcanus.core.util.CanBeDiscombobulated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyArg(method = "updateLookDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"), index = 0)
    public double changeLookDirectionX(double x) {
        if (client.player instanceof CanBeDiscombobulated player && player.isDiscombobulated())
            return -x;

        return x;
    }

    @ModifyArg(method = "updateLookDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"), index = 1)
    public double changeLookDirectionY(double y) {
        if (client.player instanceof CanBeDiscombobulated player && player.isDiscombobulated())
            return -y;

        return y;
    }
}

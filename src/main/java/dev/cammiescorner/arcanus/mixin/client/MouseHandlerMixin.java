package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.component.ArcanusComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 0)
    public double changeLookDirectionX(double x) {
        if (minecraft.player.getComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED).isDiscombobulated())
            return -x;

        return x;
    }

    @ModifyArg(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index = 1)
    public double changeLookDirectionY(double y) {
        if (minecraft.player.getComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED).isDiscombobulated())
            return -y;

        return y;
    }
}

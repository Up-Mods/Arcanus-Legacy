package dev.cammiescorner.arcanus.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @ModifyReturnValue(method = "getVisibleVehicleHeartRows", at = @At("RETURN"))
    public int getHeartRows(int original) {
        if (ArcanusClient.shouldRenderManaBar())
            return original + 1;
        return original;
    }
}

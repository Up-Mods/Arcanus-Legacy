package dev.cammiescorner.arcanus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean hasBurnout(boolean original, Player player) {
        return original && player.getComponent(ArcanusComponents.MAGIC_CASTER).getBurnout() <= 0;
    }
}

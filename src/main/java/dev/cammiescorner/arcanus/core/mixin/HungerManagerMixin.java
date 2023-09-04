package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//FIXME use MixinExtras
@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @ModifyVariable(method = "update", at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z", ordinal = 0
    ))
    public boolean hasBurnout(boolean bl, PlayerEntity player) {
        return ((MagicUser) player).getBurnout() <= 0 && player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
    }
}

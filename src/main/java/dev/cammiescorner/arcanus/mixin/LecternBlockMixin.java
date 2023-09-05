package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {

    // have to do it this way, else the max JVM stack size is exceeded
    @Inject(method = "openScreen", at = @At(value = "RETURN"))
    public void openScreen(Level world, BlockPos pos, Player player, CallbackInfo info) {
        ArcanusHelper.onInteractLecternBlock(world, pos, player);
    }
}

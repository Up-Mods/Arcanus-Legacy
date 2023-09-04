package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {

    // have to do it this way, else the max JVM stack size is exceeded
    @Inject(method = "openScreen", at = @At(value = "RETURN"))
    public void openScreen(World world, BlockPos pos, PlayerEntity player, CallbackInfo info) {
        ArcanusHelper.onInteractLecternBlock(world, pos, player);
    }
}

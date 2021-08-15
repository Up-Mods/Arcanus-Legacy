package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LecternBlock.class)
public class LecternBlockMixin{
	@Inject(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void openScreen(World world, BlockPos pos, PlayerEntity player, CallbackInfo info, BlockEntity blockEntity) {
		ItemStack stack = ((LecternBlockEntity) blockEntity).getBook();

		if(!world.isClient())
			((MagicUser) player).setKnownSpell(new Identifier(stack.getOrCreateNbt().getString("spell")));
	}
}

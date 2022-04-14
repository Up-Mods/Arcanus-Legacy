package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.common.registry.ArcanusSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	public void arcanus$growAmethyst(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
		boolean isSmallBud = state.getBlock() == Blocks.SMALL_AMETHYST_BUD;
		boolean isMedBud = state.getBlock() == Blocks.MEDIUM_AMETHYST_BUD;
		boolean isLargeBud = state.getBlock() == Blocks.LARGE_AMETHYST_BUD;

		if(isSmallBud || isMedBud || isLargeBud) {
			ItemStack stack = player.getStackInHand(hand);

			if(stack.isOf(Items.AMETHYST_SHARD)) {
				float growthChance = 0.5F;

				if(!player.isCreative())
					stack.decrement(1);

				world.playSound(null, pos, ArcanusSounds.BLOCK_AMETHYST_CRYSTAL_GROW, SoundCategory.BLOCKS, 1F, 1F);

				if(isSmallBud) {
					if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
						world.setBlockState(pos, Blocks.MEDIUM_AMETHYST_BUD.getStateWithProperties(state));

					info.setReturnValue(ActionResult.success(world.isClient));
				}

				if(isMedBud) {
					if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
						world.setBlockState(pos, Blocks.LARGE_AMETHYST_BUD.getStateWithProperties(state));

					info.setReturnValue(ActionResult.success(world.isClient));
				}

				if(isLargeBud) {
					if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
						world.setBlockState(pos, Blocks.AMETHYST_CLUSTER.getStateWithProperties(state));

					info.setReturnValue(ActionResult.success(world.isClient));
				}
			}
		}
	}
}

package dev.cammiescorner.arcanus.mixin;

import net.minecraft.block.AmethystBlock;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AmethystClusterBlock.class)
public abstract class AmethystClusterBlockMixin extends AmethystBlock {
	public AmethystClusterBlockMixin(Settings settings) { super(settings); }

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);

		if(stack.isOf(Items.AMETHYST_SHARD) && this != Blocks.AMETHYST_CLUSTER) {
			float growthChance = 0.5F;
			
			if(!player.isCreative())
				stack.decrement(1);

			if(this == Blocks.SMALL_AMETHYST_BUD) {
				if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
					world.setBlockState(pos, Blocks.MEDIUM_AMETHYST_BUD.getStateWithProperties(state));

				return ActionResult.success(world.isClient);
			}

			if(this == Blocks.MEDIUM_AMETHYST_BUD) {
				if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
					world.setBlockState(pos, Blocks.LARGE_AMETHYST_BUD.getStateWithProperties(state));

				return ActionResult.success(world.isClient);
			}

			if(this == Blocks.LARGE_AMETHYST_BUD) {
				if(!world.isClient() && world.getRandom().nextFloat() < growthChance)
					world.setBlockState(pos, Blocks.AMETHYST_CLUSTER.getStateWithProperties(state));

				return ActionResult.success(world.isClient);
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}
}

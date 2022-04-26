package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.common.registry.ArcanusSounds;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	public void arcanus$growAmethyst(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
		if((Object) this instanceof AmethystClusterBlock amethyst && amethyst != Blocks.AMETHYST_CLUSTER) {
			ItemStack stack = player.getStackInHand(hand);

			if(stack.isOf(Items.AMETHYST_SHARD)) {
				float growthChance = 0.5F;

				if(!player.isCreative())
					stack.decrement(1);

				world.playSound(null, pos, ArcanusSounds.BLOCK_AMETHYST_CRYSTAL_GROW, SoundCategory.BLOCKS, 1F, 1F);

				if(!world.isClient() && world.getRandom().nextFloat() < growthChance) {
					switch(Registry.BLOCK.getId(amethyst).toString()) {
						case "minecraft:large_amethyst_bud" ->
								world.setBlockState(pos, Blocks.AMETHYST_CLUSTER.getStateWithProperties(state));
						case "minecraft:medium_amethyst_bud" ->
								world.setBlockState(pos, Blocks.LARGE_AMETHYST_BUD.getStateWithProperties(state));
						case "minecraft:small_amethyst_bud" ->
								world.setBlockState(pos, Blocks.MEDIUM_AMETHYST_BUD.getStateWithProperties(state));
						default -> { }
					}
				}

				info.setReturnValue(ActionResult.success(world.isClient));
			}
		}
	}
}

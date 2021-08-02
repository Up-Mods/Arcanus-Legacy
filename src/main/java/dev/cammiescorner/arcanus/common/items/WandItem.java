package dev.cammiescorner.arcanus.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandItem extends Item {
	public WandItem(Settings settings) {
		super(settings);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 100;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return false;
	}
}

package dev.cammiescorner.arcanus.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WandItem extends Item
{
	public WandItem(Properties settings)
	{
		super(settings);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state)
	{
		return 100;
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner)
	{
		return false;
	}
}

package dev.cammiescorner.arcanus.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WandItem extends Item
{
	public WandItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state)
	{
		return 0;
	}
}

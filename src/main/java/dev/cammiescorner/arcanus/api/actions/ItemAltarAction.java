package dev.cammiescorner.arcanus.api.actions;

import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import org.jetbrains.annotations.Nullable;

public class ItemAltarAction implements AltarAction {
	private ItemStack stack = ItemStack.EMPTY;

	@Override
	public void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
		if(stack != null && !stack.isEmpty())
			ItemScatterer.spawn(world, altar.getPos().getX() + 0.5, altar.getPos().getY() + 0.5, altar.getPos().getZ() + 0.5, stack);
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
}

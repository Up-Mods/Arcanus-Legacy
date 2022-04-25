package dev.cammiescorner.arcanus.api.actions;

import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ItemAltarAction implements AltarAction {
	private ItemStack stack = ItemStack.EMPTY;

	@Override
	public void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
		if(stack != null && !stack.isEmpty()) {
			ItemEntity entity = new ItemEntity(world, altar.getPos().getX() + 0.5, altar.getPos().getY() + 1.9, altar.getPos().getZ() + 0.5, stack);
			entity.setNoGravity(true);
			entity.setVelocity(Vec3d.ZERO);
			world.spawnEntity(entity);
		}
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
}

package dev.cammiescorner.arcanus.common.blocks.entities;

import dev.cammiescorner.arcanus.core.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlockEntity extends BlockEntity implements Inventory {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.DISPLAY_CASE, pos, state);
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = Inventories.splitStack(this.inventory, slot, amount);
		this.notifyListeners();
		return stack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = Inventories.removeStack(this.inventory, slot);
		this.notifyListeners();
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);
		this.notifyListeners();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return !(player.squaredDistanceTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 64.0D);
	}

	@Override
	public void clear() {
		this.inventory.clear();
		this.notifyListeners();
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound tag = super.toInitialChunkDataNbt();
		writeNbt(tag);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void notifyListeners() {
		this.markDirty();

		if(world != null && !world.isClient())
			world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.inventory.clear();
		Inventories.readNbt(nbt, this.inventory);
		super.readNbt(nbt);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, this.inventory);
		super.writeNbt(nbt);
	}
}

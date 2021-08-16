package dev.cammiescorner.arcanus.common.blocks.entities;

import dev.cammiescorner.arcanus.common.blocks.FillableBookshelfBlock;
import dev.cammiescorner.arcanus.common.screens.BookshelfScreenHandler;
import dev.cammiescorner.arcanus.core.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class FillableBookshelfBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);

	public FillableBookshelfBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FILLABLE_BOOKSHELF, pos, state);
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
		return player.squaredDistanceTo(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 16;
	}

	@Override
	public void clear() {
		this.inventory.clear();
		this.notifyListeners();
	}

	public void notifyListeners() {
		this.markDirty();
		this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState().with(FillableBookshelfBlock.BOOK_COUNT, fullSlots()), Block.NOTIFY_ALL);
	}

	public int fullSlots() {
		int count = 0;

		for(ItemStack stack : inventory)
			if(!stack.isEmpty())
				count++;

		return count;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.inventory.clear();
		Inventories.readNbt(nbt, this.inventory);
		super.readNbt(nbt);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, this.inventory);
		return super.writeNbt(nbt);
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new BookshelfScreenHandler(syncId, playerInventory, this);
	}
}

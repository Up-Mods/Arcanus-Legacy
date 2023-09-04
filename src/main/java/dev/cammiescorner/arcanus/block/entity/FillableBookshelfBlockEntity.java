package dev.cammiescorner.arcanus.block.entity;

import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import dev.cammiescorner.arcanus.screen.BookshelfScreenHandler;
import dev.cammiescorner.arcanus.registry.ArcanusBlockEntities;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class FillableBookshelfBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);

    public FillableBookshelfBlockEntity(BlockPos pos, BlockState state) {
        super(ArcanusBlockEntities.FILLABLE_BOOKSHELF, pos, state);
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

    public void notifyListeners() {
        this.markDirty();

        if (world != null) {
            BlockState oldState = getCachedState();
            world.setBlockState(getPos(), world.getBlockState(getPos()).with(FillableBookshelfBlock.BOOK_COUNT, fullSlots()));
            world.updateListeners(getPos(), oldState, getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public int fullSlots() {
        int count = 0;

        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
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
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        super.writeNbt(nbt);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BookshelfScreenHandler(syncId, playerInventory, this);
    }
}

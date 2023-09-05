package dev.cammiescorner.arcanus.block.entity;

import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import dev.cammiescorner.arcanus.screen.BookshelfScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import dev.cammiescorner.arcanus.registry.ArcanusBlockEntities;
import org.jetbrains.annotations.Nullable;

public class FillableBookshelfBlockEntity extends BlockEntity implements MenuProvider, Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(16, ItemStack.EMPTY);

    public FillableBookshelfBlockEntity(BlockPos pos, BlockState state) {
        super(ArcanusBlockEntities.FILLABLE_BOOKSHELF, pos, state);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.inventory, slot, amount);
        this.notifyListeners();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = ContainerHelper.takeItem(this.inventory, slot);
        this.notifyListeners();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        this.notifyListeners();
    }

    @Override
    public boolean stillValid(Player player) {
        return !(player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) > 64.0D);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
        this.notifyListeners();
    }

    public void notifyListeners() {
        this.setChanged();

        if (level != null) {
            BlockState oldState = getBlockState();
            level.setBlockAndUpdate(getBlockPos(), level.getBlockState(getBlockPos()).setValue(FillableBookshelfBlock.BOOK_COUNT, fullSlots()));
            level.sendBlockUpdated(getBlockPos(), oldState, getBlockState(), Block.UPDATE_ALL);
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
    public void load(CompoundTag nbt) {
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, this.inventory);
        super.load(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, this.inventory);
        super.saveAdditional(nbt);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new BookshelfScreenHandler(syncId, playerInventory, this);
    }
}

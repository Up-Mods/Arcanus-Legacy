package dev.cammiescorner.arcanus.block.entity;

import dev.cammiescorner.arcanus.registry.ArcanusBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlockEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public DisplayCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ArcanusBlockEntities.DISPLAY_CASE.get(), pos, state);
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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void notifyListeners() {
        this.setChanged();

        if (level != null && !level.isClientSide())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
}

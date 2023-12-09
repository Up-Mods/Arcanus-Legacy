package dev.cammiescorner.arcanus.screen;

import dev.cammiescorner.arcanus.registry.ArcanusScreens;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BookshelfScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public BookshelfScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(16));
    }

    public BookshelfScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(ArcanusScreens.BOOKSHELF_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(inventory, 16);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        int x;
        int y;

        // Bookshelf Inventory
        for (y = 0; y < 2; ++y)
            for (x = 0; x < 8; ++x)
                this.addSlot(new BookSlot(inventory, x + y * 8, 17 + x * 18, 26 + y * 18));

        // Player Inventory
        for (y = 0; y < 3; ++y)
            for (x = 0; x < 9; ++x)
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

        // Player Hotbar
        for (y = 0; y < 9; ++y)
            this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 142));
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean bl = false;
        int i = startIndex;

        if (fromLast) {
            i = endIndex - 1;
        }

        Slot slot2;
        ItemStack itemStack;

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                slot2 = this.slots.get(i);
                itemStack = slot2.getItem();

                if (!itemStack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemStack)) {
                    int j = itemStack.getCount() + stack.getCount();
                    int maxCount = Math.min(stack.getMaxStackSize(), slot2.getMaxStackSize());

                    if (j <= maxCount) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot2.setChanged();
                        bl = true;
                    } else if (itemStack.getCount() < maxCount) {
                        stack.shrink(maxCount - itemStack.getCount());
                        itemStack.setCount(maxCount);
                        slot2.setChanged();
                        bl = true;
                    }
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (fromLast) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                slot2 = this.slots.get(i);
                itemStack = slot2.getItem();

                if (itemStack.isEmpty() && slot2.mayPlace(stack)) {
                    if (stack.getCount() > slot2.getMaxStackSize()) {
                        slot2.set(stack.split(slot2.getMaxStackSize()));
                    } else {
                        slot2.set(stack.split(stack.getCount()));
                    }

                    slot2.setChanged();
                    bl = true;

                    break;
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return bl;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false))
                return ItemStack.EMPTY;

            if (originalStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        return newStack;
    }

    public static class BookSlot extends Slot {
        private static final TagKey<Item> BOOKS = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c", "books"));

        public BookSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(BOOKS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void set(ItemStack stack) {
            super.set(stack);
        }
    }
}

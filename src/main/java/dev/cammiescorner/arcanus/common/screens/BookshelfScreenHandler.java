package dev.cammiescorner.arcanus.common.screens;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BookshelfScreenHandler extends ScreenHandler {
	private final Inventory inventory;

	public BookshelfScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(16));
	}

	public BookshelfScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(Arcanus.BOOKSHELF_SCREEN_HANDLER, syncId);
		checkSize(inventory, 16);
		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);

		int x;
		int y;

		// Bookshelf Inventory
		for(y = 0; y < 2; ++y)
			for(x = 0; x < 8; ++x)
				this.addSlot(new BookSlot(inventory, x + y * 8, 17 + x * 18, 26 + y * 18));

		// Player Inventory
		for(y = 0; y < 3; ++y)
			for(x = 0; x < 9; ++x)
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

		// Player Hotbar
		for(y = 0; y < 9; ++y)
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 142));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
		boolean bl = false;
		int i = startIndex;

		if(fromLast) {
			i = endIndex - 1;
		}

		Slot slot2;
		ItemStack itemStack;

		if(stack.isStackable()) {
			while(!stack.isEmpty()) {
				if(fromLast) {
					if(i < startIndex) {
						break;
					}
				}
				else if(i >= endIndex) {
					break;
				}

				slot2 = this.slots.get(i);
				itemStack = slot2.getStack();

				if(!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
					int j = itemStack.getCount() + stack.getCount();
					int maxCount = Math.min(stack.getMaxCount(), slot2.getMaxItemCount());

					if(j <= maxCount) {
						stack.setCount(0);
						itemStack.setCount(j);
						slot2.markDirty();
						bl = true;
					}
					else if(itemStack.getCount() < maxCount) {
						stack.decrement(maxCount - itemStack.getCount());
						itemStack.setCount(maxCount);
						slot2.markDirty();
						bl = true;
					}
				}

				if(fromLast) {
					--i;
				}
				else {
					++i;
				}
			}
		}

		if(!stack.isEmpty()) {
			if(fromLast) {
				i = endIndex - 1;
			}
			else {
				i = startIndex;
			}

			while(true) {
				if(fromLast) {
					if(i < startIndex) {
						break;
					}
				}
				else if(i >= endIndex) {
					break;
				}

				slot2 = this.slots.get(i);
				itemStack = slot2.getStack();

				if(itemStack.isEmpty() && slot2.canInsert(stack)) {
					if(stack.getCount() > slot2.getMaxItemCount()) {
						slot2.setStack(stack.split(slot2.getMaxItemCount()));
					}
					else {
						slot2.setStack(stack.split(stack.getCount()));
					}

					slot2.markDirty();
					bl = true;

					break;
				}

				if(fromLast) {
					--i;
				}
				else {
					++i;
				}
			}
		}

		return bl;
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);

		if(slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();

			if(invSlot < this.inventory.size()) {
				if(!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true))
					return ItemStack.EMPTY;
			}
			else if(!this.insertItem(originalStack, 0, this.inventory.size(), false))
				return ItemStack.EMPTY;

			if(originalStack.isEmpty())
				slot.setStack(ItemStack.EMPTY);
			else
				slot.markDirty();
		}

		return newStack;
	}

	public static class BookSlot extends Slot {
		private static final Tag<Item> BOOKS = TagRegistry.item(new Identifier("c", "books"));

		public BookSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return stack.isIn(BOOKS);
		}

		@Override
		public int getMaxItemCount() {
			return 1;
		}
	}
}

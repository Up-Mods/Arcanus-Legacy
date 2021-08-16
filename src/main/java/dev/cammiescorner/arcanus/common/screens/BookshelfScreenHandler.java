package dev.cammiescorner.arcanus.common.screens;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

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
		for(x = 0; x < 2; ++x)
			for(y = 0; y < 8; ++y)
				this.addSlot(new BookSlot(inventory, y + x * 3, 17 + y * 18, 26 + x * 18));

		// Player Inventory
		for(x = 0; x < 3; ++x)
			for(y = 0; y < 9; ++y)
				this.addSlot(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));

		// Player Hotbar
		for(x = 0; x < 9; ++x)
			this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int invSlot) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(invSlot);

		if(slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();

			if(invSlot < this.inventory.size())
				if(!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true))
					return ItemStack.EMPTY;
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
		public BookSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return stack.isOf(Items.ENCHANTED_BOOK) || stack.isOf(Items.WRITTEN_BOOK);
		}
	}
}

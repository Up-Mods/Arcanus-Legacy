package dev.cammiescorner.arcanus.common.blocks.entities;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
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
import net.minecraft.tag.FluidTags;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.*;

public class AmethystAltarBlockEntity extends BlockEntity implements Inventory {
	private static final List<BlockPos> AMETHYST_POSES = List.of(
			new BlockPos(0, 1, -3), new BlockPos(2, 1, -2), new BlockPos(3, 1, 0), new BlockPos(2, 1, 2),
			new BlockPos(0, 1, 3), new BlockPos(-2, 1, 2), new BlockPos(-3, 1, 0), new BlockPos(-2, 1, -2)
	);
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
	private boolean crafting, completed;

	public AmethystAltarBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.AMETHYST_ALTAR, pos, state);

	}

	public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
		if(blockEntity instanceof AmethystAltarBlockEntity altar) {
			if(world.getTime() % 20 == 0 && altar.isCompleted())
				altar.checkMultiblock();
		}
	}

	@Override
	public int size() {
		return inventory.size();
	}

	public int filledSlots() {
		int i = 0;

		while(i < size() && !getStack(i).isEmpty()) {
			++i;
		}

		return i;
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = Inventories.splitStack(inventory, slot, amount);
		notifyListeners();
		return stack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = Inventories.removeStack(inventory, slot);
		notifyListeners();
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		notifyListeners();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return !(player.squaredDistanceTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 64.0D);
	}

	@Override
	public void clear() {
		inventory.clear();
		notifyListeners();
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
		markDirty();

		if(world != null && !world.isClient())
			world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		inventory.clear();
		Inventories.readNbt(nbt, inventory);
		completed = nbt.getBoolean("Completed");
		crafting = nbt.getBoolean("Active");
		super.readNbt(nbt);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, inventory);
		nbt.putBoolean("Completed", completed);
		nbt.putBoolean("Active", crafting);
		super.writeNbt(nbt);
	}

	public DefaultedList<ItemStack> getInventory() {
		return inventory;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
		this.crafting = crafting;
		notifyListeners();
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;

		if(world != null) {
			Set<PurpleWaterComponent> set = new HashSet<>();

			for(Map.Entry<BlockPos, BlockState> entry : ArcanusHelper.getStructureMap(world).entrySet()) {
				if(entry.getValue().getFluidState().isIn(FluidTags.WATER)) {
					BlockPos.Mutable waterPos = entry.getKey().mutableCopy().move(pos).move(-5, 0, -5);
					Chunk waterChunk = world.getChunk(waterPos);
					PurpleWaterComponent component = ArcanusComponents.PURPLE_WATER_COMPONENT.get(waterChunk);
					set.add(component);

					if(completed)
						component.addAltar(waterPos.toImmutable(), getPos());
				}
			}

			for(PurpleWaterComponent component : set) {
				if(!completed)
					component.removeAltar(getPos());

				ArcanusComponents.PURPLE_WATER_COMPONENT.sync(world.getChunk(getPos()));
			}
		}

		notifyListeners();
	}

	public void checkMultiblock() {
		if(world != null && !world.isClient()) {
			HashMap<BlockPos, BlockState> map = new HashMap<>();

			for(int y = 0; y < 6; y++) {
				for(int x = 0; x < 11; x++) {
					for(int z = 0; z < 11; z++) {
						BlockPos.Mutable pos = getPos().mutableCopy().move(-5, 0, -5).move(x, y, z);
						BlockState state = world.getBlockState(pos);

						if(ArcanusHelper.isValidAltarBlock(state))
							map.put(new BlockPos(x, y, z), state);
					}
				}
			}

			setCompleted(map.equals(ArcanusHelper.getStructureMap(world)));
		}
	}
}

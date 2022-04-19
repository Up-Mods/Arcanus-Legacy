package dev.cammiescorner.arcanus.common.blocks.entities;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.client.particles.ArcanusBlockParticle;
import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.*;

public class AmethystAltarBlockEntity extends BlockEntity implements Inventory {
	private static final List<BlockPos> AMETHYST_POS_LIST = List.of(
			new BlockPos(0, 1, -3), new BlockPos(3, 1, -3), new BlockPos(3, 1, 0), new BlockPos(3, 1, 3),
			new BlockPos(0, 1, 3), new BlockPos(-3, 1, 3), new BlockPos(-3, 1, 0), new BlockPos(-3, 1, -3)
	);
	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
	private boolean crafting, completed;
	private int power, craftingTime, amethystIndex;

	public AmethystAltarBlockEntity(BlockPos pos, BlockState state) {
		super(ArcanusBlockEntities.AMETHYST_ALTAR, pos, state);

	}

	public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T blockEntity) {
		if(blockEntity instanceof AmethystAltarBlockEntity altar && altar.isCompleted()) {
			if(world.getTime() % 20 == 0)
				altar.checkMultiblock();

			Box box = state.getCollisionShape(world, pos).getBoundingBox().stretch(2, 0.4, 2).stretch(-3, 0, -3).offset(altar.getPos());
			List<ItemEntity> list = world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), box, itemEntity -> altar.filledSlots() < altar.size());

			for(ItemEntity itemEntity : list) {
				ItemStack stack = itemEntity.getStack();
				altar.setStack(altar.filledSlots(), stack.split(1));

				if(stack.getCount() <= 0)
					itemEntity.discard();
			}

			if(altar.isCrafting()) {
				altar.craftingTime++;

				if(altar.power < 10) {
					if(altar.amethystIndex >= 8)
						altar.amethystIndex = 0;

					BlockPos amethystPos = AMETHYST_POS_LIST.get(altar.amethystIndex).add(altar.getPos());
					BlockState amethystState = world.getBlockState(amethystPos);

					if(!(amethystState.getBlock() instanceof AmethystClusterBlock)) {
						altar.amethystIndex++;
						return;
					}

					if(altar.craftingTime % 30 == 0) {
						world.breakBlock(amethystPos, false);

						if(amethystState.getBlock() == Blocks.AMETHYST_CLUSTER)
							world.setBlockState(amethystPos, Blocks.LARGE_AMETHYST_BUD.getDefaultState());
						else if(amethystState.getBlock() == Blocks.LARGE_AMETHYST_BUD)
							world.setBlockState(amethystPos, Blocks.MEDIUM_AMETHYST_BUD.getDefaultState());
						else if(amethystState.getBlock() == Blocks.MEDIUM_AMETHYST_BUD)
							world.setBlockState(amethystPos, Blocks.SMALL_AMETHYST_BUD.getDefaultState());
						else
							world.setBlockState(amethystPos, Blocks.AIR.getDefaultState());

						altar.power++;
						altar.amethystIndex++;
					}

					if(world.isClient()) {
						MinecraftClient client = MinecraftClient.getInstance();
						BlockPos upperCrystal = altar.getPos().add(0, 2, 0);
						Vec3d direction = new Vec3d(upperCrystal.getX() - amethystPos.getX(), upperCrystal.getY() - amethystPos.getY(), upperCrystal.getZ() - amethystPos.getZ());
						ArcanusBlockParticle particle = new ArcanusBlockParticle(client.world, amethystPos.getX() + 0.5, amethystPos.getY() + 0.75, amethystPos.getZ() + 0.5, direction.getX(), direction.getY(), direction.getZ(), amethystState, amethystPos);
						particle.move(2.5F);
						client.particleManager.addParticle(particle);
					}
				}
			}
			else {
				altar.craftingTime = 0;
				altar.amethystIndex = 0;
				altar.power = 0;
			}
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
		power = nbt.getInt("Power");
		craftingTime = nbt.getInt("CraftingTime");
		amethystIndex = nbt.getInt("AmethystIndex");
		super.readNbt(nbt);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, inventory);
		nbt.putBoolean("Completed", completed);
		nbt.putBoolean("Active", crafting);
		nbt.putInt("Power", power);
		nbt.putInt("CraftingTime", craftingTime);
		nbt.putInt("AmethystIndex", amethystIndex);
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
					BlockPos.Mutable waterPos = entry.getKey().mutableCopy().move(pos).move(ArcanusHelper.getAltarOffset(world));
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
			HashMap<BlockPos, BlockState> structure = new HashMap<>();
			HashMap<BlockPos, BlockState> template = ArcanusHelper.getStructureMap(world);

			for(Map.Entry<BlockPos, BlockState> entry : template.entrySet()) {
				BlockPos.Mutable pos = getPos().mutableCopy().move(ArcanusHelper.getAltarOffset(world)).move(entry.getKey());
				BlockState state = world.getBlockState(pos);

				if(ArcanusHelper.isValidAltarBlock(state))
					structure.put(entry.getKey(), state);
			}

			setCompleted(structure.equals(template));
		}
	}

	public int getPower() {
		return power;
	}

	public int getCraftingTime() {
		return craftingTime;
	}
}

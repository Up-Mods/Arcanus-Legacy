package dev.cammiescorner.arcanus.common.blocks;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AmethystAltarBlock extends Block implements Waterloggable, BlockEntityProvider {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 8, 16);

	public AmethystAltarBlock() {
		super(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).requiresTool().nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.getBlockEntity(pos) instanceof AmethystAltarBlockEntity altar && altar.canPlayerUse(player)) {
			if(!ArcanusHelper.isCasting(player)) {
				ItemStack stack = player.getStackInHand(hand);

				if(!player.isSneaking() && !stack.isEmpty() && altar.getStack(altar.size() - 1).isEmpty()) {
					for(int i = 0; i < altar.size(); i++) {
						if(!altar.getStack(i).isEmpty())
							continue;

						altar.setStack(i, stack.split(1));
						break;
					}

					if(!player.isCreative())
						stack.decrement(1);

					return ActionResult.success(world.isClient);
				}
				else if(player.isSneaking() && !altar.getStack(0).isEmpty()) {
					for(int i = altar.size() - 1; i >= 0; i--) {
						ItemStack altarStack = altar.getStack(i);

						if(altarStack.isEmpty())
							continue;

						boolean canInsert = player.getInventory().insertStack(altarStack);
						ItemEntity itemEntity;

						if(!canInsert || !altarStack.isEmpty()) {
							itemEntity = player.dropItem(altarStack, false);

							if(itemEntity == null)
								continue;

							itemEntity.resetPickupDelay();
							itemEntity.setOwner(player.getUuid());
							continue;
						}

						itemEntity = player.dropItem(altarStack, false);

						if(itemEntity != null)
							itemEntity.setDespawnImmediately();

						player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1F) * 2F);
						altar.removeStack(i);

						break;
					}

					return ActionResult.success(world.isClient);
				}
			}
			else {
				if(altar.isCompleted())
					altar.setActive(!altar.isActive());
				else
					altar.checkMultiblock();

				return ActionResult.success(world.isClient);
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);

		if(!world.isClient() && state.getBlock() != newState.getBlock()) {
			Set<PurpleWaterComponent> set = new HashSet<>();

			for(Map.Entry<BlockPos, BlockState> entry : ArcanusHelper.getStructureMap(world).entrySet()) {
				if(entry.getValue().getFluidState().isIn(FluidTags.WATER)) {
					BlockPos waterPos = entry.getKey().add(pos).add(-5, 0, -5);
					Chunk waterChunk = world.getChunk(waterPos);
					PurpleWaterComponent component = ArcanusComponents.PURPLE_WATER_COMPONENT.get(waterChunk);
					set.add(component);
				}
			}

			for(PurpleWaterComponent component : set) {
				component.removeAltar(pos);
				ArcanusComponents.PURPLE_WATER_COMPONENT.sync(component.getChunk());
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if(state.get(Properties.WATERLOGGED))
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AmethystAltarBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return AmethystAltarBlockEntity::tick;
	}
}

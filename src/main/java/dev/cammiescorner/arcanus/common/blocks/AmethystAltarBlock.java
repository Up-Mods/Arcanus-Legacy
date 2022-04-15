package dev.cammiescorner.arcanus.common.blocks;

import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class AmethystAltarBlock extends Block implements Waterloggable, BlockEntityProvider {
	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 8, 16);

	public AmethystAltarBlock() {
		super(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).requiresTool().nonOpaque());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.getBlockEntity(pos) instanceof AmethystAltarBlockEntity altar && altar.canPlayerUse(player)) {
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
				for(int i = 0; i < altar.size(); i++) {
					ItemStack altarStack = altar.getStack(i);

					if(!altarStack.isEmpty())
						continue;

					player.getInventory().setStack(player.getInventory().getEmptySlot(), altarStack);
					altar.removeStack(i);
					break;
				}

				return ActionResult.success(world.isClient);
			}
		}

		return super.onUse(state, world, pos, player, hand, hit);
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

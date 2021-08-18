package dev.cammiescorner.arcanus.common.blocks;

import dev.cammiescorner.arcanus.common.blocks.entities.DisplayCaseBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlock extends BlockWithEntity {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = Properties.OPEN;
	private static final VoxelShape SHAPE = VoxelShapes.union(
			createCuboidShape(2, 0, 2, 14, 2, 14),
			createCuboidShape(5, 0, 1, 11, 4, 15),
			createCuboidShape(1, 0, 5, 15, 4, 11),
			createCuboidShape(4, 2, 4, 12, 12, 12),
			createCuboidShape(2, 12, 2, 14, 16, 14)
	);

	public DisplayCaseBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient() && player.isSneaking()) {
			if(state.get(OPEN))
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, (float) MathHelper.clamp(1 - (MinecraftClient.getInstance().player.getPos().distanceTo(Vec3d.ofCenter(pos)) / 10F), 0, 1), world.random.nextFloat() * 0.1F + 0.9F, false);
			else
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, (float) MathHelper.clamp(1 - (MinecraftClient.getInstance().player.getPos().distanceTo(Vec3d.ofCenter(pos)) / 10F), 0, 1), world.random.nextFloat() * 0.1F + 0.9F, false);
		}

		if(!world.isClient() && world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity blockEntity) {
			if(player.isSneaking()) {
				world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
				return ActionResult.SUCCESS;
			}

			if(state.get(OPEN)) {
				ItemStack handStack = player.getStackInHand(hand);
				ItemStack entityStack = blockEntity.removeStack(0);

				if(ItemStack.areItemsEqual(handStack, entityStack) && handStack.getCount() < handStack.getMaxCount()) {
					handStack.increment(1);
					return ActionResult.SUCCESS;
				}

				ItemStack splitStack = handStack.split(1);

				if(handStack.isEmpty())
					player.setStackInHand(hand, entityStack);
				else if(!entityStack.isEmpty() && !player.getInventory().insertStack(entityStack))
					player.dropItem(entityStack, true);

				blockEntity.setStack(0, splitStack);
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(OPEN, false);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if(state.getBlock() != newState.getBlock()) {
			if(world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity displayCase) {
				ItemScatterer.spawn(world, pos, displayCase);
				world.updateComparators(pos, this);
			}

			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(OPEN) ? 5 : 0;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DisplayCaseBlockEntity(pos, state);
	}
}

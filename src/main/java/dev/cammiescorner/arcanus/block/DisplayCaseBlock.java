package dev.cammiescorner.arcanus.block;

import dev.cammiescorner.arcanus.block.entity.DisplayCaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.*;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DisplayCaseBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private static final VoxelShape SHAPE = Shapes.or(
            box(2, 0, 2, 14, 2, 14),
            box(5, 0, 1, 11, 4, 15),
            box(1, 0, 5, 15, 4, 11),
            box(4, 2, 4, 12, 12, 12),
            box(1, 12, 1, 15, 16, 15)
    );

    public DisplayCaseBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Deprecated
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide() && player.isShiftKeyDown()) {
            if (state.getValue(OPEN))
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, (float) Mth.clamp(1 - (Minecraft.getInstance().player.position().distanceTo(Vec3.atCenterOf(pos)) / 10F), 0, 1), world.random.nextFloat() * 0.1F + 0.9F, false);
            else
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, (float) Mth.clamp(1 - (Minecraft.getInstance().player.position().distanceTo(Vec3.atCenterOf(pos)) / 10F), 0, 1), world.random.nextFloat() * 0.1F + 0.9F, false);
        }

        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity blockEntity) {
            if (player.isShiftKeyDown()) {
                world.setBlockAndUpdate(pos, state.setValue(OPEN, !state.getValue(OPEN)));
                return InteractionResult.SUCCESS;
            }

            if (state.getValue(OPEN)) {
                ItemStack handStack = player.getItemInHand(hand);
                ItemStack entityStack = blockEntity.removeItemNoUpdate(0);

                if (ItemStack.isSameItemSameTags(handStack, entityStack) && handStack.getCount() < handStack.getMaxStackSize()) {
                    handStack.grow(1);
                    return InteractionResult.SUCCESS;
                }

                ItemStack splitStack = handStack.split(1);

                if (handStack.isEmpty())
                    player.setItemInHand(hand, entityStack);
                else if (!entityStack.isEmpty() && !player.getInventory().add(entityStack))
                    player.drop(entityStack, true);

                blockEntity.setItem(0, splitStack);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Deprecated
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape directionalTop = state.getValue(FACING) == Direction.NORTH ?
                Shapes.or(
                        box(1, 13, 1, 2, 16, 15),
                        box(14, 13, 1, 15, 16, 15),
                        box(2, 13, 14, 14, 16, 15)
                ) : state.getValue(FACING) == Direction.EAST ?
                Shapes.or(
                        box(1, 13, 1, 15, 16, 2),
                        box(1, 13, 14, 15, 16, 15),
                        box(1, 13, 2, 2, 16, 14)
                ) : state.getValue(FACING) == Direction.SOUTH ?
                Shapes.or(
                        box(1, 13, 1, 2, 16, 15),
                        box(14, 13, 1, 15, 16, 15),
                        box(2, 13, 1, 14, 16, 2)
                ) :
                Shapes.or(
                        box(1, 13, 1, 15, 16, 2),
                        box(1, 13, 14, 15, 16, 15),
                        box(14, 13, 2, 15, 16, 14)
                );

        VoxelShape openShape = Shapes.or(
                box(2, 0, 2, 14, 2, 14),
                box(5, 0, 1, 11, 4, 15),
                box(1, 0, 5, 15, 4, 11),
                box(4, 2, 4, 12, 12, 12),
                box(1, 12, 1, 15, 13, 15),
                directionalTop
        );

        return state.getValue(OPEN) ? openShape : SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(OPEN, false);
    }

    @Deprecated
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Deprecated
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof DisplayCaseBlockEntity displayCase) {
                Containers.dropContents(world, pos, displayCase);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Deprecated
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Deprecated
    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(OPEN) ? 5 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayCaseBlockEntity(pos, state);
    }
}

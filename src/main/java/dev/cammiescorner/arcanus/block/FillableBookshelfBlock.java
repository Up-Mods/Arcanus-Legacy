package dev.cammiescorner.arcanus.block;

import dev.cammiescorner.arcanus.block.entity.FillableBookshelfBlockEntity;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FillableBookshelfBlock extends BaseEntityBlock implements BlockItemProvider {
    public static final IntegerProperty BOOK_COUNT = IntegerProperty.create("book_count", 0, 16);

    public FillableBookshelfBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(BOOK_COUNT, 0));
    }

    @Override
    public Item createItem() {
        return new BlockItem(this, new Item.Properties());
    }

    @Deprecated
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);

            if (screenHandlerFactory != null)
                player.openMenu(screenHandlerFactory);
        }

        return InteractionResult.SUCCESS;
    }

    @Deprecated
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof FillableBookshelfBlockEntity bookshelf) {
                Containers.dropContents(world, pos, bookshelf);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Deprecated
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Deprecated
    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOOK_COUNT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FillableBookshelfBlockEntity(pos, state);
    }
}

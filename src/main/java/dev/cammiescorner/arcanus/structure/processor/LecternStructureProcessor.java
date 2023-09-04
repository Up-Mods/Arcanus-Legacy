package dev.cammiescorner.arcanus.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.registry.ArcanusStructureProcessors;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class LecternStructureProcessor extends StructureProcessor {
    public static final LecternStructureProcessor INSTANCE = new LecternStructureProcessor();
    public static final Codec<LecternStructureProcessor> CODEC = Codec.unit(() -> LecternStructureProcessor.INSTANCE);

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo localBlockInfo, Structure.StructureBlockInfo absoluteBlockInfo, StructurePlacementData placementData) {
        if (!absoluteBlockInfo.state.isOf(Blocks.LECTERN))
            return absoluteBlockInfo;

        absoluteBlockInfo.nbt.put("Book", SpellBooks.getRandomSpellBook(placementData.getRandom(absoluteBlockInfo.pos)).writeNbt(new NbtCompound()));

        return new Structure.StructureBlockInfo(absoluteBlockInfo.pos, absoluteBlockInfo.state.with(LecternBlock.HAS_BOOK, true), absoluteBlockInfo.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ArcanusStructureProcessors.LECTERN_PROCESSOR;
    }
}

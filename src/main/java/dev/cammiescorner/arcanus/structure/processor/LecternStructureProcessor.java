package dev.cammiescorner.arcanus.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.registry.ArcanusStructureProcessors;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class LecternStructureProcessor extends StructureProcessor {
    public static final LecternStructureProcessor INSTANCE = new LecternStructureProcessor();
    public static final Codec<LecternStructureProcessor> CODEC = Codec.unit(() -> LecternStructureProcessor.INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo localBlockInfo, StructureTemplate.StructureBlockInfo absoluteBlockInfo, StructurePlaceSettings placementData) {
        if (!absoluteBlockInfo.state.is(Blocks.LECTERN))
            return absoluteBlockInfo;

        absoluteBlockInfo.nbt.put("Book", SpellBooks.getRandomSpellBook(placementData.getRandom(absoluteBlockInfo.pos)).save(new CompoundTag()));

        return new StructureTemplate.StructureBlockInfo(absoluteBlockInfo.pos, absoluteBlockInfo.state.setValue(LecternBlock.HAS_BOOK, true), absoluteBlockInfo.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ArcanusStructureProcessors.LECTERN_PROCESSOR;
    }
}

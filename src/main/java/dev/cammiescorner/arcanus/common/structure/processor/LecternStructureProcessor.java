package dev.cammiescorner.arcanus.common.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
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
	public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureInfoLocal, Structure.StructureBlockInfo structureInfoWorld, StructurePlacementData data) {
		if(!structureInfoWorld.state.isOf(Blocks.LECTERN))
			return structureInfoWorld;

		structureInfoWorld.nbt.put("Book", SpellBooks.getRandomSpellBook().writeNbt(new NbtCompound()));

		return new Structure.StructureBlockInfo(structureInfoWorld.pos, structureInfoWorld.state.with(LecternBlock.HAS_BOOK, true), structureInfoWorld.nbt);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return Arcanus.LECTERN_PROCESSOR;
	}
}

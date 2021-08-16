package dev.cammiescorner.arcanus.common.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class BookshelfReplacerStructureProcessor extends StructureProcessor {
	public static final BookshelfReplacerStructureProcessor INSTANCE = new BookshelfReplacerStructureProcessor();
	public static final Codec<BookshelfReplacerStructureProcessor> CODEC = Codec.unit(() -> BookshelfReplacerStructureProcessor.INSTANCE);

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureInfoLocal, Structure.StructureBlockInfo structureInfoWorld, StructurePlacementData data) {
		return structureInfoWorld;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return Arcanus.BOOKSHELF_PROCESSOR;
	}
}

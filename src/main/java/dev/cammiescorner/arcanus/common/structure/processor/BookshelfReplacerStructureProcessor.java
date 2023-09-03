package dev.cammiescorner.arcanus.common.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.FillableBookshelfBlock;
import dev.cammiescorner.arcanus.core.registry.ModBlocks;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class BookshelfReplacerStructureProcessor extends StructureProcessor {
	public static final BookshelfReplacerStructureProcessor INSTANCE = new BookshelfReplacerStructureProcessor();
	public static final Codec<BookshelfReplacerStructureProcessor> CODEC = Codec.unit(() -> BookshelfReplacerStructureProcessor.INSTANCE);

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureInfoLocal, Structure.StructureBlockInfo structureInfoWorld, StructurePlacementData data) {
		RandomGenerator random = data.getRandom(structureInfoWorld.pos);

		if(!structureInfoWorld.state.isOf(Blocks.BOOKSHELF) || random.nextInt(5) > 0)
			return structureInfoWorld;

		int bookCount = random.nextInt(5);
		BlockState state = ModBlocks.BOOKSHELF.getDefaultState();
		DefaultedList<ItemStack> inventory = DefaultedList.ofSize(16, ItemStack.EMPTY);
		NbtCompound nbt = new NbtCompound();

		for(int i = 0; i < bookCount; ++i)
			inventory.set(random.nextInt(16), SpellBooks.getRandomSpellBook(random));

		state = state.with(FillableBookshelfBlock.BOOK_COUNT, bookCount);
		Inventories.writeNbt(nbt, inventory);

		return new Structure.StructureBlockInfo(structureInfoWorld.pos, state, nbt);
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return Arcanus.BOOKSHELF_PROCESSOR;
	}
}

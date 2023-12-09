package dev.cammiescorner.arcanus.structure.processor;

import com.mojang.serialization.Codec;
import dev.cammiescorner.arcanus.block.FillableBookshelfBlock;
import dev.cammiescorner.arcanus.registry.ArcanusBlocks;
import dev.cammiescorner.arcanus.registry.ArcanusStructureProcessors;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class BookshelfReplacerStructureProcessor extends StructureProcessor {
    public static final BookshelfReplacerStructureProcessor INSTANCE = new BookshelfReplacerStructureProcessor();
    public static final Codec<BookshelfReplacerStructureProcessor> CODEC = Codec.unit(() -> BookshelfReplacerStructureProcessor.INSTANCE);

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo structureInfoLocal, StructureTemplate.StructureBlockInfo structureInfoWorld, StructurePlaceSettings data) {
        RandomSource random = data.getRandom(structureInfoWorld.pos());

        if (!structureInfoWorld.state().is(Blocks.BOOKSHELF) || random.nextInt(5) > 0)
            return structureInfoWorld;

        int bookCount = random.nextInt(5);
        BlockState state = ArcanusBlocks.BOOKSHELF.get().defaultBlockState();
        NonNullList<ItemStack> inventory = NonNullList.withSize(16, ItemStack.EMPTY);
        CompoundTag nbt = new CompoundTag();

        for (int i = 0; i < bookCount; ++i)
            inventory.set(random.nextInt(16), SpellBooks.getRandomSpellBook(random));

        state = state.setValue(FillableBookshelfBlock.BOOK_COUNT, bookCount);
        ContainerHelper.saveAllItems(nbt, inventory);

        return new StructureTemplate.StructureBlockInfo(structureInfoWorld.pos(), state, nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ArcanusStructureProcessors.BOOKSHELF_PROCESSOR.get();
    }
}

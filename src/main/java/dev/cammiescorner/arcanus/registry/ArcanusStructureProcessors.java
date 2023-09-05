package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.structure.processor.LecternStructureProcessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ArcanusStructureProcessors {

    public static final StructureProcessorType<LecternStructureProcessor> LECTERN_PROCESSOR = () -> LecternStructureProcessor.CODEC;
    public static final StructureProcessorType<BookshelfReplacerStructureProcessor> BOOKSHELF_PROCESSOR = () -> BookshelfReplacerStructureProcessor.CODEC;

    public static void register() {
        Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, Arcanus.id("set_lectern_book"), LECTERN_PROCESSOR);
        Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, Arcanus.id("replace_bookshelf"), BOOKSHELF_PROCESSOR);
    }
}

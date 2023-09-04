package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.structure.processor.LecternStructureProcessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.processor.StructureProcessorType;

public class ArcanusStructureProcessors {

    public static final StructureProcessorType<LecternStructureProcessor> LECTERN_PROCESSOR = () -> LecternStructureProcessor.CODEC;
    public static final StructureProcessorType<BookshelfReplacerStructureProcessor> BOOKSHELF_PROCESSOR = () -> BookshelfReplacerStructureProcessor.CODEC;

    public static void register() {
        Registry.register(Registries.STRUCTURE_PROCESSOR_TYPE, Arcanus.id("set_lectern_book"), LECTERN_PROCESSOR);
        Registry.register(Registries.STRUCTURE_PROCESSOR_TYPE, Arcanus.id("replace_bookshelf"), BOOKSHELF_PROCESSOR);
    }
}

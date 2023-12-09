package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.structure.processor.LecternStructureProcessor;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ArcanusStructureProcessors {

    public static final RegistryHandler<StructureProcessorType<?>> STRUCTURE_PROCESSORS = RegistryHandler.create(Registries.STRUCTURE_PROCESSOR, Arcanus.MODID);

    public static final RegistrySupplier<StructureProcessorType<LecternStructureProcessor>> LECTERN_PROCESSOR = STRUCTURE_PROCESSORS.register("set_lectern_book", () -> () -> LecternStructureProcessor.CODEC);
    public static final RegistrySupplier<StructureProcessorType<BookshelfReplacerStructureProcessor>> BOOKSHELF_PROCESSOR = STRUCTURE_PROCESSORS.register("replace_bookshelf", () -> () -> BookshelfReplacerStructureProcessor.CODEC);
}

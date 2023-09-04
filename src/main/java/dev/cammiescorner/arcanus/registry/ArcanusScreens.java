package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.screen.BookshelfScreenHandler;
import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ArcanusScreens {

    public static final ScreenHandlerType<BookshelfScreenHandler> BOOKSHELF_SCREEN_HANDLER = new ScreenHandlerType<>(BookshelfScreenHandler::new, FeatureFlags.DEFAULT_SET);

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER_TYPE, Arcanus.id("fillable_bookshelf"), BOOKSHELF_SCREEN_HANDLER);
    }
}

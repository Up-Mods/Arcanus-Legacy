package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.screen.BookshelfScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ArcanusScreens {

    public static final MenuType<BookshelfScreenHandler> BOOKSHELF_SCREEN_HANDLER = new MenuType<>(BookshelfScreenHandler::new, FeatureFlags.VANILLA_SET);

    public static void register() {
        Registry.register(BuiltInRegistries.MENU, Arcanus.id("fillable_bookshelf"), BOOKSHELF_SCREEN_HANDLER);
    }
}

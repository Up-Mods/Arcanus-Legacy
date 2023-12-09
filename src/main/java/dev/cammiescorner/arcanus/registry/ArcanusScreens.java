package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.screen.BookshelfScreenHandler;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ArcanusScreens {

    public static final RegistryHandler<MenuType<?>> SCREENS = RegistryHandler.create(Registries.MENU, Arcanus.MODID);

    public static final RegistrySupplier<MenuType<BookshelfScreenHandler>> BOOKSHELF_SCREEN_HANDLER = SCREENS.register("fillable_bookshelf", () -> new MenuType<>(BookshelfScreenHandler::new, FeatureFlags.VANILLA_SET));
}

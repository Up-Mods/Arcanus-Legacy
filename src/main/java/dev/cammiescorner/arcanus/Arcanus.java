package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.common.packets.CastSpellPacket;
import dev.cammiescorner.arcanus.common.screens.BookshelfScreenHandler;
import dev.cammiescorner.arcanus.common.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.common.structure.processor.LecternStructureProcessor;
import dev.cammiescorner.arcanus.core.integration.ArcanusConfig;
import dev.cammiescorner.arcanus.core.registry.*;
import dev.cammiescorner.arcanus.core.util.ArcanusLootFunction;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import dev.cammiescorner.arcanus.core.util.Pattern;
import dev.cammiescorner.arcanus.core.util.Spell;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import java.util.List;

public class Arcanus implements ModInitializer {

    public static final String MOD_ID = "arcanus";
    public static final Logger LOGGER = LogManager.getLogger("Arcanus");

    public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKey.ofRegistry(id("spell"));
    public static final Registry<Spell> SPELL = FabricRegistryBuilder.createSimple(SPELL_KEY).buildAndRegister();

    public static final LootFunctionType ARCANUS_LOOT_FUNCTION = new LootFunctionType(new ArcanusLootFunction.Serializer());

    public static final StructureProcessorType<LecternStructureProcessor> LECTERN_PROCESSOR = StructureProcessorType.register("set_lectern_book", LecternStructureProcessor.CODEC);
    public static final StructureProcessorType<BookshelfReplacerStructureProcessor> BOOKSHELF_PROCESSOR = StructureProcessorType.register("replace_bookshelf", BookshelfReplacerStructureProcessor.CODEC);

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static MutableText translate(@Nullable String prefix, String... value) {
        return Text.translatable(translationKey(prefix, value));
    }

    public static MutableText translate(@Nullable String prefix, String value, Object... args) {
        return Text.translatable(translationKey(prefix, value), null, args);
    }

    public static String translationKey(@Nullable String prefix, String... value) {
        String translationKey = Arcanus.MOD_ID + "." + String.join(".", value);
        return prefix != null ? (prefix + "." + translationKey) : translationKey;
    }

    public static final ScreenHandlerType<BookshelfScreenHandler> BOOKSHELF_SCREEN_HANDLER = new ScreenHandlerType<>(BookshelfScreenHandler::new, FeatureFlags.DEFAULT_SET);

    public static MutableText getSpellInputs(List<Pattern> pattern, int index) {
        return index >= pattern.size() || pattern.get(index) == null ? Text.literal("?").formatted(Formatting.GRAY, Formatting.UNDERLINE) : Text.literal(pattern.get(index).getSymbol()).formatted(Formatting.GREEN);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        DataTrackers.MANA.getId();
        MidnightConfig.init(MOD_ID, ArcanusConfig.class);

        org.quiltmc.qsl.networking.api.ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handle);
        Registry.register(Registries.LOOK_FUNCTION_TYPE, id("arcanus_loot_function"), ARCANUS_LOOT_FUNCTION);
        Registry.register(Registries.ENTITY_ATTRIBUTE, id("casting_multiplier"), EntityAttributes.MANA_COST);
        Registry.register(Registries.ENTITY_ATTRIBUTE, id("mana_regen"), EntityAttributes.MANA_REGEN);
        Registry.register(Registries.ENTITY_ATTRIBUTE, id("burnout_regen"), EntityAttributes.BURNOUT_REGEN);
        Registry.register(Registries.ENTITY_ATTRIBUTE, id("mana_lock"), EntityAttributes.MANA_LOCK);

        Registry.register(Registries.SCREEN_HANDLER_TYPE, id("fillable_bookshelf"), BOOKSHELF_SCREEN_HANDLER);

        ModItems.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModSpells.register();
        ModEntities.register();
        ModParticles.register();
        ModSoundEvents.register();
        EventHandler.commonEvents();

        LOGGER.info("imagine people still looking for these :hahayes:");
    }

    public static class DataTrackers {
        public static final TrackedData<Integer> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Integer> BURNOUT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Boolean> SHOW_MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    public static class EntityAttributes {
        public static final EntityAttribute MANA_COST = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_cost"), 1D, 0D, 1024D).setTracked(true);
        public static final EntityAttribute MANA_REGEN = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_regen"), 1D, 0D, 1024D).setTracked(true);
        public static final EntityAttribute BURNOUT_REGEN = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "burnout_regen"), 1D, 0D, 1024D).setTracked(true);
        public static final EntityAttribute MANA_LOCK = new ClampedEntityAttribute(Arcanus.translationKey("attribute.name.generic", "mana_lock"), 0D, 0D, 20D).setTracked(true);
    }


}

package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.net.CastSpellPacket;
import dev.cammiescorner.arcanus.registry.*;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.EventHandler;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.List;

public class Arcanus implements ModInitializer {

    public static final String MODID = "arcanus";
    public static final Logger LOGGER = LogManager.getLogger("Arcanus");

    public static final ResourceKey<Registry<Spell>> SPELL_KEY = ResourceKey.createRegistryKey(id("spell"));
    public static final Registry<Spell> SPELL = FabricRegistryBuilder.createSimple(SPELL_KEY).buildAndRegister();

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static MutableComponent translate(@Nullable String prefix, String... value) {
        return Component.translatable(translationKey(prefix, value));
    }

    public static MutableComponent translate(@Nullable String prefix, String value, Object... args) {
        return Component.translatableWithFallback(translationKey(prefix, value), null, args);
    }

    public static String translationKey(@Nullable String prefix, String... value) {
        String translationKey = Arcanus.MODID + "." + String.join(".", value);
        return prefix != null ? (prefix + "." + translationKey) : translationKey;
    }

    public static MutableComponent getSpellInputs(List<Spell.Pattern> pattern, int index) {
        return index >= pattern.size() || pattern.get(index) == null ? Component.literal("?").withStyle(ChatFormatting.GRAY, ChatFormatting.UNDERLINE) : Component.literal(pattern.get(index).getSymbol()).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        MidnightConfig.init(MODID, ArcanusConfig.class);

        ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handle);

        RegistryService registryService = RegistryService.get();
        ArcanusEntityAttributes.register(registryService);
        ArcanusEntities.ENTITIES.accept(registryService);
        ArcanusBlocks.BLOCKS.accept(registryService);
        ArcanusItems.CREATIVE_TABS.accept(registryService);
        ArcanusItems.ITEMS.accept(registryService);
        ArcanusBlockEntities.BLOCK_ENTITIES.accept(registryService);
        ArcanusParticles.PARTICLES.accept(registryService);
        ArcanusScreens.SCREENS.accept(registryService);
        ArcanusLootFunctions.LOOT_FUNCTIONS.accept(registryService);
        ArcanusSoundEvents.SOUND_EVENTS.accept(registryService);
        ArcanusStructureProcessors.STRUCTURE_PROCESSORS.accept(registryService);
        ArcanusSpells.SPELLS.accept(registryService);
        ArcanusCommands.register();

        EventHandler.commonEvents();

        LOGGER.info("imagine people still looking for these :hahayes:");
    }


}

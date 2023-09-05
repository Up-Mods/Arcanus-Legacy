package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.net.CastSpellPacket;
import dev.cammiescorner.arcanus.registry.*;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.EventHandler;
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

    public static final String MOD_ID = "arcanus";
    public static final Logger LOGGER = LogManager.getLogger("Arcanus");

    public static final ResourceKey<Registry<Spell>> SPELL_KEY = ResourceKey.createRegistryKey(id("spell"));
    public static final Registry<Spell> SPELL = FabricRegistryBuilder.createSimple(SPELL_KEY).buildAndRegister();

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static MutableComponent translate(@Nullable String prefix, String... value) {
        return Component.translatable(translationKey(prefix, value));
    }

    public static MutableComponent translate(@Nullable String prefix, String value, Object... args) {
        return Component.translatableWithFallback(translationKey(prefix, value), null, args);
    }

    public static String translationKey(@Nullable String prefix, String... value) {
        String translationKey = Arcanus.MOD_ID + "." + String.join(".", value);
        return prefix != null ? (prefix + "." + translationKey) : translationKey;
    }

    public static MutableComponent getSpellInputs(List<Spell.Pattern> pattern, int index) {
        return index >= pattern.size() || pattern.get(index) == null ? Component.literal("?").withStyle(ChatFormatting.GRAY, ChatFormatting.UNDERLINE) : Component.literal(pattern.get(index).getSymbol()).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        MidnightConfig.init(MOD_ID, ArcanusConfig.class);

        ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handle);

        ArcanusBlocks.register();
        ArcanusItems.register();
        ArcanusBlockEntities.register();
        ArcanusCommands.register();
        ArcanusEntities.register();
        ArcanusEntityAttributes.register();
        ArcanusLootFunctions.register();
        ArcanusParticles.register();
        ArcanusScreens.register();
        ArcanusSoundEvents.register();
        ArcanusSpells.register();
        ArcanusStructureProcessors.register();

        EventHandler.commonEvents();

        LOGGER.info("imagine people still looking for these :hahayes:");
    }


}

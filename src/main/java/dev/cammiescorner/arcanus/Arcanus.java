package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.net.CastSpellPacket;
import dev.cammiescorner.arcanus.registry.*;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.EventHandler;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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

    public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKey.ofRegistry(id("spell"));
    public static final Registry<Spell> SPELL = FabricRegistryBuilder.createSimple(SPELL_KEY).buildAndRegister();

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

    public static MutableText getSpellInputs(List<Spell.Pattern> pattern, int index) {
        return index >= pattern.size() || pattern.get(index) == null ? Text.literal("?").formatted(Formatting.GRAY, Formatting.UNDERLINE) : Text.literal(pattern.get(index).getSymbol()).formatted(Formatting.GREEN);
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

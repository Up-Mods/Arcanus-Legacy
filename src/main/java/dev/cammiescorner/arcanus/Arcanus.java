package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.EventHandler;
import dev.cammiescorner.arcanus.common.packets.c2s.CastSpellPacket;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

public class Arcanus implements ModInitializer {
	public static final DefaultedRegistry<Spell> SPELL = FabricRegistryBuilder.createDefaulted(Spell.class, id("spell"), id("empty")).buildAndRegister();
	public static final String MOD_ID = "arcanus";

	@Override
	public void onInitialize() {
		ArcanusSpells.register();
		ArcanusCommands.register();

		Registry.register(Registry.ATTRIBUTE, id("casting_multiplier"), ArcanusAttributes.AURA_COST);
		Registry.register(Registry.ATTRIBUTE, id("aura_regen"), ArcanusAttributes.AURA_REGEN);
		Registry.register(Registry.ATTRIBUTE, id("aura_lock"), ArcanusAttributes.AURA_LOCK);
		Registry.register(Registry.ATTRIBUTE, id("enhancement_affinity"), ArcanusAttributes.ENHANCEMENT_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("transmutation_affinity"), ArcanusAttributes.TRANSMUTATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("emission_affinity"), ArcanusAttributes.EMISSION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("conjuration_affinity"), ArcanusAttributes.CONJURATION_AFFINITY);
		Registry.register(Registry.ATTRIBUTE, id("manipulation_affinity"), ArcanusAttributes.MANIPULATION_AFFINITY);

		ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SetCastingPacket.ID, SetCastingPacket::handler);

		EventHandler.commonEvents();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}

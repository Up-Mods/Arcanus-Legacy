package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.EventHandler;
import dev.cammiescorner.arcanus.common.packets.c2s.CastSpellPacket;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
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

		Registry.register(Registry.ATTRIBUTE, id("casting_multiplier"), EntityAttributes.AURA_COST);
		Registry.register(Registry.ATTRIBUTE, id("aura_regen"), EntityAttributes.AURA_REGEN);
		Registry.register(Registry.ATTRIBUTE, id("aura_lock"), EntityAttributes.AURA_LOCK);

		ServerPlayNetworking.registerGlobalReceiver(CastSpellPacket.ID, CastSpellPacket::handler);
		ServerPlayNetworking.registerGlobalReceiver(SetCastingPacket.ID, SetCastingPacket::handler);

		EventHandler.commonEvents();
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static class EntityAttributes {
		public static final EntityAttribute AURA_COST = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".aura_cost", 1D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute AURA_REGEN = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".aura_regen", 60D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute AURA_LOCK = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + "aura_lock", 0D, 0D, 20D).setTracked(true);
	}
}

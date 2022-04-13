package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ArcanusSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	public static final Spell EMPTY = create("empty", new Spell(AuraType.NONE, SpellComplexity.UNIQUE, 0));
	public static final Spell ENHANCER = create("enhancer", new Spell(AuraType.ENHANCER, SpellComplexity.SIMPLE, 10));
	public static final Spell TRANSMUTER = create("transmuter", new Spell(AuraType.TRANSMUTER, SpellComplexity.SIMPLE, 10));
	public static final Spell EMITTER = create("emitter", new Spell(AuraType.EMITTER, SpellComplexity.SIMPLE, 10));
	public static final Spell CONJURER = create("conjurer", new Spell(AuraType.CONJURER, SpellComplexity.SIMPLE, 10));
	public static final Spell MANIPULATOR = create("manipulator", new Spell(AuraType.MANIPULATOR, SpellComplexity.SIMPLE, 10));
	public static final Spell SPECIALIST = create("specialist", new Spell(AuraType.SPECIALIST, SpellComplexity.SIMPLE, 10));

	//-----Registry-----//
	public static void register() {
		SPELLS.forEach((spell, id) -> Registry.register(Arcanus.SPELL, id, spell));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

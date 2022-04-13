package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ArcanusSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	public static final Spell EMPTY = create("empty", new Spell(Spell.Type.NONE, Spell.Complexity.UNIQUE, 0));
	public static final Spell ENHANCER = create("enhancer", new Spell(Spell.Type.ENHANCER, Spell.Complexity.SIMPLE, 10));
	public static final Spell TRANSMUTER = create("transmuter", new Spell(Spell.Type.TRANSMUTER, Spell.Complexity.SIMPLE, 10));
	public static final Spell EMITTER = create("emitter", new Spell(Spell.Type.EMITTER, Spell.Complexity.SIMPLE, 10));
	public static final Spell CONJURER = create("conjurer", new Spell(Spell.Type.CONJURER, Spell.Complexity.SIMPLE, 10));
	public static final Spell MANIPULATOR = create("manipulator", new Spell(Spell.Type.MANIPULATOR, Spell.Complexity.SIMPLE, 10));
	public static final Spell SPECIALIST = create("specialist", new Spell(Spell.Type.SPECIALIST, Spell.Complexity.SIMPLE, 10));

	//-----Registry-----//
	public static void register() {
		SPELLS.forEach((spell, id) -> Registry.register(Arcanus.SPELL, id, spell));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

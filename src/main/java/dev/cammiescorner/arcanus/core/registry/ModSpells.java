package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

import static dev.cammiescorner.arcanus.core.util.Pattern.LEFT;
import static dev.cammiescorner.arcanus.core.util.Pattern.RIGHT;

public class ModSpells
{
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	//-----Spells-----//
	public static final Spell LUNGE = create("lunge", new Spell(RIGHT, RIGHT, RIGHT, 0));
	public static final Spell FISSURE = create("fissure", new Spell(LEFT, RIGHT, LEFT, 0));
	public static final Spell MAGIC_MISSILE = create("magic_missile", new Spell(LEFT, LEFT, LEFT, 0));
	public static final Spell VANISH = create("vanish", new Spell(RIGHT, LEFT, RIGHT, 0));
	public static final Spell HEAL = create("heal", new Spell(RIGHT, LEFT, LEFT, 0));
	public static final Spell METEOR = create("meteor", new Spell(LEFT, RIGHT, RIGHT, 0));
	public static final Spell ICE_SPIRE = create("ice_spire", new Spell(LEFT, LEFT, RIGHT, 0));
	public static final Spell MINE = create("mine", new Spell(RIGHT, RIGHT, LEFT, 0));

	//-----Registry-----//
	public static void register()
	{
		SPELLS.keySet().forEach(item -> Registry.register(Arcanus.SPELL, SPELLS.get(item), item));
	}

	private static <T extends Spell> T create(String name, T spell)
	{
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

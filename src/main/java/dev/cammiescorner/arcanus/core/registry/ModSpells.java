package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

import static dev.cammiescorner.arcanus.core.util.Pattern.*;
import static dev.cammiescorner.arcanus.Arcanus.*;

public class ModSpells
{
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	//-----Spells-----//
	public static final Spell LUNGE = create("lunge", new Spell(RIGHT, RIGHT, RIGHT, config.lungeCastingCost));
	public static final Spell FISSURE = create("fissure", new Spell(LEFT, RIGHT, LEFT, config.fissureCastingCost));
	public static final Spell MAGIC_MISSILE = create("magic_missile", new Spell(LEFT, LEFT, LEFT, config.magicMissileCastingCost));
	public static final Spell VANISH = create("vanish", new Spell(RIGHT, LEFT, RIGHT, config.vanishCastingCost));
	public static final Spell HEAL = create("heal", new Spell(RIGHT, LEFT, LEFT, config.healCastingCost));
	public static final Spell METEOR = create("meteor", new Spell(LEFT, RIGHT, RIGHT, config.meteorCastingCost));
	public static final Spell ICE_SPIRE = create("ice_spire", new Spell(LEFT, LEFT, RIGHT, config.iceSpireCastingCost));
	public static final Spell MINE = create("mine", new Spell(RIGHT, RIGHT, LEFT, config.mineCastingCost));

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

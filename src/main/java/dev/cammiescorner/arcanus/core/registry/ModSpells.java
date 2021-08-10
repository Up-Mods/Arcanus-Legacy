package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.*;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

import static dev.cammiescorner.arcanus.Arcanus.config;
import static dev.cammiescorner.arcanus.core.util.Pattern.LEFT;
import static dev.cammiescorner.arcanus.core.util.Pattern.RIGHT;

public class ModSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	//-----Spells-----//
	public static final Spell LUNGE = create("lunge", new LungeSpell(RIGHT, RIGHT, RIGHT, config.lungeCastingCost));
	public static final Spell DREAM_WARP = create("dream_warp", new DreamWarpSpell(LEFT, RIGHT, LEFT, config.dreamWarpCastingCost));
	public static final Spell MAGIC_MISSILE = create("magic_missile", new MagicMissileSpell(LEFT, LEFT, LEFT, config.magicMissileCastingCost));
	public static final Spell TELEKINESIS = create("telekinetic_shock", new TelekinesisSpell(RIGHT, LEFT, RIGHT, config.telekinesisCastingCost));
	public static final Spell HEAL = create("heal", new HealSpell(RIGHT, LEFT, LEFT, config.healCastingCost));
	public static final Spell METEOR = create("meteor", new MeteorSpell(LEFT, RIGHT, RIGHT, config.meteorCastingCost));
	public static final Spell SOLAR_STRIKE = create("solar_strike", new SolarStrikeSpell(LEFT, LEFT, RIGHT, config.solarStrikeCastingCost));
	public static final Spell ARCANE_WALL = create("arcane_wall", new ArcaneWallSpell(RIGHT, RIGHT, LEFT, config.arcaneWallCastingCost));

	//-----Registry-----//
	public static void register() {
		SPELLS.keySet().forEach(item -> Registry.register(Arcanus.SPELL, SPELLS.get(item), item));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

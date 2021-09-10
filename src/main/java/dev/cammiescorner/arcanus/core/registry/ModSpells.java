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
	public static final Spell LUNGE = create("lunge", new LungeSpell(RIGHT, RIGHT, RIGHT, config.manaCosts.lungeCastingCost));
	public static final Spell DREAM_WARP = create("dream_warp", new DreamWarpSpell(LEFT, RIGHT, LEFT, config.manaCosts.dreamWarpCastingCost));
	public static final Spell MAGIC_MISSILE = create("magic_missile", new MagicMissileSpell(LEFT, LEFT, LEFT, config.manaCosts.magicMissileCastingCost));
	public static final Spell TELEKINESIS = create("telekinetic_shock", new TelekinesisSpell(RIGHT, LEFT, RIGHT, config.manaCosts.telekinesisCastingCost));
	public static final Spell HEAL = create("heal", new HealSpell(RIGHT, LEFT, LEFT, config.manaCosts.healCastingCost));
	public static final Spell DISCOMBOBULATE = create("discombobulate", new DiscombobulateSpell(LEFT, RIGHT, RIGHT, config.manaCosts.discombobulateCastingCost));
	public static final Spell SOLAR_STRIKE = create("solar_strike", new SolarStrikeSpell(LEFT, LEFT, RIGHT, config.manaCosts.solarStrikeCastingCost));
	public static final Spell ARCANE_BARRIER = create("arcane_barrier", new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, config.manaCosts.arcaneBarrierCastingCost));

	//-----Registry-----//
	public static void register() {
		SPELLS.keySet().forEach(item -> Registry.register(Arcanus.SPELL, SPELLS.get(item), item));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

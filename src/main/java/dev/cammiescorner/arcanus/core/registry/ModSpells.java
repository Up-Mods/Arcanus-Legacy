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
	public static final Spell LUNGE = new LungeSpell(RIGHT, RIGHT, RIGHT, config.spells.manaCosts.lungeCastingCost);
	public static final Spell DREAM_WARP = new DreamWarpSpell(RIGHT, LEFT, RIGHT, config.spells.manaCosts.dreamWarpCastingCost);
	public static final Spell MAGIC_MISSILE = new MagicMissileSpell(LEFT, LEFT, LEFT, config.spells.manaCosts.magicMissileCastingCost);
	public static final Spell TELEKINESIS = new TelekinesisSpell(LEFT, RIGHT, LEFT, config.spells.manaCosts.telekinesisCastingCost);
	public static final Spell HEAL = new HealSpell(RIGHT, LEFT, LEFT, config.spells.manaCosts.healCastingCost);
	public static final Spell DISCOMBOBULATE = new DiscombobulateSpell(LEFT, RIGHT, RIGHT, config.spells.manaCosts.discombobulateCastingCost);
	public static final Spell SOLAR_STRIKE = new SolarStrikeSpell(LEFT, LEFT, RIGHT, config.spells.manaCosts.solarStrikeCastingCost);
	public static final Spell ARCANE_BARRIER = new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, config.spells.manaCosts.arcaneBarrierCastingCost);

	//-----Registry-----//
	public static void register() {
		if(config.spells.enableLunge)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "lunge"), LUNGE);
		if(config.spells.enableDreamWarp)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "dream_warp"), DREAM_WARP);
		if(config.spells.enableMagicMissile)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "magic_missile"), MAGIC_MISSILE);
		if(config.spells.enableTelekineticShock)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "telekinetic_shock"), TELEKINESIS);
		if(config.spells.enableHeal)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "heal"), HEAL);
		if(config.spells.enableDiscombobulate)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "discombobulate"), DISCOMBOBULATE);
		if(config.spells.enableSolarStrike)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "solar_strike"), SOLAR_STRIKE);
		if(config.spells.enableArcaneBarrier)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "arcane_barrier"), ARCANE_BARRIER);
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

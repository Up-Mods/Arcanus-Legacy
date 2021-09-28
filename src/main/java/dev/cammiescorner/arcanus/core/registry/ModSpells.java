package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.*;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

import static dev.cammiescorner.arcanus.Arcanus.*;
import static dev.cammiescorner.arcanus.core.util.Pattern.LEFT;
import static dev.cammiescorner.arcanus.core.util.Pattern.RIGHT;

public class ModSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	//-----Spells-----//
	public static final Spell LUNGE = new LungeSpell(RIGHT, RIGHT, RIGHT, getConfig().spells.manaCosts.lungeCastingCost);
	public static final Spell DREAM_WARP = new DreamWarpSpell(RIGHT, LEFT, RIGHT, getConfig().spells.manaCosts.dreamWarpCastingCost);
	public static final Spell MAGIC_MISSILE = new MagicMissileSpell(LEFT, LEFT, LEFT, getConfig().spells.manaCosts.magicMissileCastingCost);
	public static final Spell TELEKINESIS = new TelekinesisSpell(LEFT, RIGHT, LEFT, getConfig().spells.manaCosts.telekinesisCastingCost);
	public static final Spell HEAL = new HealSpell(RIGHT, LEFT, LEFT, getConfig().spells.manaCosts.healCastingCost);
	public static final Spell DISCOMBOBULATE = new DiscombobulateSpell(LEFT, RIGHT, RIGHT, getConfig().spells.manaCosts.discombobulateCastingCost);
	public static final Spell SOLAR_STRIKE = new SolarStrikeSpell(LEFT, LEFT, RIGHT, getConfig().spells.manaCosts.solarStrikeCastingCost);
	public static final Spell ARCANE_BARRIER = new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, getConfig().spells.manaCosts.arcaneBarrierCastingCost);

	//-----Registry-----//
	public static void register() {
		if(getConfig().spells.enableLunge)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "lunge"), LUNGE);
		if(getConfig().spells.enableDreamWarp)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "dream_warp"), DREAM_WARP);
		if(getConfig().spells.enableMagicMissile)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "magic_missile"), MAGIC_MISSILE);
		if(getConfig().spells.enableTelekineticShock)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "telekinetic_shock"), TELEKINESIS);
		if(getConfig().spells.enableHeal)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "heal"), HEAL);
		if(getConfig().spells.enableDiscombobulate)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "discombobulate"), DISCOMBOBULATE);
		if(getConfig().spells.enableSolarStrike)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "solar_strike"), SOLAR_STRIKE);
		if(getConfig().spells.enableArcaneBarrier)
			Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "arcane_barrier"), ARCANE_BARRIER);
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

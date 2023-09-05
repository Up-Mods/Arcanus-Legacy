package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.spell.*;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import net.minecraft.core.Registry;

import static dev.cammiescorner.arcanus.spell.Spell.Pattern.LEFT;
import static dev.cammiescorner.arcanus.spell.Spell.Pattern.RIGHT;

public class ArcanusSpells {

    public static final Spell LUNGE = new LungeSpell(RIGHT, RIGHT, RIGHT, ArcanusConfig.lungeCastingCost);
    public static final Spell DREAM_WARP = new DreamWarpSpell(RIGHT, LEFT, RIGHT, ArcanusConfig.dreamWarpCastingCost);
    public static final Spell MAGIC_MISSILE = new MagicMissileSpell(LEFT, LEFT, LEFT, ArcanusConfig.magicMissileCastingCost);
    public static final Spell TELEKINESIS = new TelekinesisSpell(LEFT, RIGHT, LEFT, ArcanusConfig.telekinesisCastingCost);
    public static final Spell HEAL = new HealSpell(RIGHT, LEFT, LEFT, ArcanusConfig.healCastingCost);
    public static final Spell DISCOMBOBULATE = new DiscombobulateSpell(LEFT, RIGHT, RIGHT, ArcanusConfig.discombobulateCastingCost);
    public static final Spell SOLAR_STRIKE = new SolarStrikeSpell(LEFT, LEFT, RIGHT, ArcanusConfig.solarStrikeCastingCost);
    public static final Spell ARCANE_BARRIER = new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, ArcanusConfig.arcaneBarrierCastingCost);

    public static void register() {
        if (ArcanusConfig.enableLunge)
            Registry.register(Arcanus.SPELL, Arcanus.id("lunge"), LUNGE);
        if (ArcanusConfig.enableDreamWarp)
            Registry.register(Arcanus.SPELL, Arcanus.id("dream_warp"), DREAM_WARP);
        if (ArcanusConfig.enableMagicMissile)
            Registry.register(Arcanus.SPELL, Arcanus.id("magic_missile"), MAGIC_MISSILE);
        if (ArcanusConfig.enableTelekineticShock)
            Registry.register(Arcanus.SPELL, Arcanus.id("telekinetic_shock"), TELEKINESIS);
        if (ArcanusConfig.enableHeal)
            Registry.register(Arcanus.SPELL, Arcanus.id("heal"), HEAL);
        if (ArcanusConfig.enableDiscombobulate)
            Registry.register(Arcanus.SPELL, Arcanus.id("discombobulate"), DISCOMBOBULATE);
        if (ArcanusConfig.enableSolarStrike)
            Registry.register(Arcanus.SPELL, Arcanus.id("solar_strike"), SOLAR_STRIKE);
        if (ArcanusConfig.enableArcaneBarrier)
            Registry.register(Arcanus.SPELL, Arcanus.id("arcane_barrier"), ARCANE_BARRIER);
    }
}

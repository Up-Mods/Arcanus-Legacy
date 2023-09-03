package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.*;
import dev.cammiescorner.arcanus.core.integration.ArcanusConfig;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.cammiescorner.arcanus.core.util.Pattern.LEFT;
import static dev.cammiescorner.arcanus.core.util.Pattern.RIGHT;

public class ModSpells {
    //-----Spells-----//
    public static final Spell LUNGE = new LungeSpell(RIGHT, RIGHT, RIGHT, ArcanusConfig.lungeCastingCost);
    public static final Spell DREAM_WARP = new DreamWarpSpell(RIGHT, LEFT, RIGHT, ArcanusConfig.dreamWarpCastingCost);
    public static final Spell MAGIC_MISSILE = new MagicMissileSpell(LEFT, LEFT, LEFT, ArcanusConfig.magicMissileCastingCost);
    public static final Spell TELEKINESIS = new TelekinesisSpell(LEFT, RIGHT, LEFT, ArcanusConfig.telekinesisCastingCost);
    public static final Spell HEAL = new HealSpell(RIGHT, LEFT, LEFT, ArcanusConfig.healCastingCost);
    public static final Spell DISCOMBOBULATE = new DiscombobulateSpell(LEFT, RIGHT, RIGHT, ArcanusConfig.discombobulateCastingCost);
    public static final Spell SOLAR_STRIKE = new SolarStrikeSpell(LEFT, LEFT, RIGHT, ArcanusConfig.solarStrikeCastingCost);
    public static final Spell ARCANE_BARRIER = new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, ArcanusConfig.arcaneBarrierCastingCost);

    //-----Registry-----//
    public static void register() {
        if (ArcanusConfig.enableLunge)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "lunge"), LUNGE);
        if (ArcanusConfig.enableDreamWarp)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "dream_warp"), DREAM_WARP);
        if (ArcanusConfig.enableMagicMissile)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "magic_missile"), MAGIC_MISSILE);
        if (ArcanusConfig.enableTelekineticShock)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "telekinetic_shock"), TELEKINESIS);
        if (ArcanusConfig.enableHeal)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "heal"), HEAL);
        if (ArcanusConfig.enableDiscombobulate)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "discombobulate"), DISCOMBOBULATE);
        if (ArcanusConfig.enableSolarStrike)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "solar_strike"), SOLAR_STRIKE);
        if (ArcanusConfig.enableArcaneBarrier)
            Registry.register(Arcanus.SPELL, new Identifier(Arcanus.MOD_ID, "arcane_barrier"), ARCANE_BARRIER);
    }
}

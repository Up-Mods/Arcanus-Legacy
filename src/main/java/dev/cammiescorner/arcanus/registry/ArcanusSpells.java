package dev.cammiescorner.arcanus.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.spell.*;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

import static dev.cammiescorner.arcanus.spell.Spell.Pattern.LEFT;
import static dev.cammiescorner.arcanus.spell.Spell.Pattern.RIGHT;

public class ArcanusSpells {

    public static final RegistryHandler<Spell> SPELLS = RegistryHandler.create(Arcanus.SPELL_KEY, Arcanus.MODID);

    public static final RegistrySupplier<Spell> LUNGE = SPELLS.register("lunge", () -> new LungeSpell(RIGHT, RIGHT, RIGHT, ArcanusConfig.lungeCastingCost));
    public static final RegistrySupplier<Spell> DREAM_WARP = SPELLS.register("dream_warp", () -> new DreamWarpSpell(RIGHT, LEFT, RIGHT, ArcanusConfig.dreamWarpCastingCost));
    public static final RegistrySupplier<Spell> MAGIC_MISSILE = SPELLS.register("magic_missile", () -> new MagicMissileSpell(LEFT, LEFT, LEFT, ArcanusConfig.magicMissileCastingCost));
    public static final RegistrySupplier<Spell> TELEKINETIC_SHOCK = SPELLS.register("telekinetic_shock", () -> new TelekinesisSpell(LEFT, RIGHT, LEFT, ArcanusConfig.telekinesisCastingCost));
    public static final RegistrySupplier<Spell> HEAL = SPELLS.register("heal", () -> new HealSpell(RIGHT, LEFT, LEFT, ArcanusConfig.healCastingCost));
    public static final RegistrySupplier<Spell> DISCOMBOBULATE = SPELLS.register("discombobulate", () -> new DiscombobulateSpell(LEFT, RIGHT, RIGHT, ArcanusConfig.discombobulateCastingCost));
    public static final RegistrySupplier<Spell> SOLAR_STRIKE = SPELLS.register("solar_strike", () -> new SolarStrikeSpell(LEFT, LEFT, RIGHT, ArcanusConfig.solarStrikeCastingCost));
    public static final RegistrySupplier<Spell> ARCANE_BARRIER = SPELLS.register("arcane_barrier", () -> new ArcaneBarrierSpell(RIGHT, RIGHT, LEFT, ArcanusConfig.arcaneBarrierCastingCost));
}

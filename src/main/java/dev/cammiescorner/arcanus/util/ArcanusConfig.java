package dev.cammiescorner.arcanus.util;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class ArcanusConfig extends MidnightConfig {
    @Entry
    public static boolean doLecternProcessor = true;
    @Entry
    public static boolean doBookshelfProcessor = true;
    @Entry
    public static boolean strongholdsHaveBooks = true;
    @Entry
    public static boolean ruinedPortalsHaveBooks = true;
    @Entry
    public static boolean haveBurnout = true;
    @Entry
    public static String magicColour = Integer.toHexString(0x7ecdfb);
    @Entry
    public static int baseManaCooldown = 20;
    @Entry
    public static int baseBurnoutCooldown = 60;
    @Entry(min = 0)
    public static int maxMana = 20;

    @Entry
    public static boolean enableLunge = true;
    @Entry
    public static boolean enableDreamWarp = true;
    @Entry
    public static boolean enableMagicMissile = true;
    @Entry
    public static boolean enableTelekineticShock = true;
    @Entry
    public static boolean enableHeal = true;
    @Entry
    public static boolean enableDiscombobulate = true;
    @Entry
    public static boolean enableSolarStrike = true;
    @Entry
    public static boolean enableArcaneBarrier = true;

    @Entry
    public static int lungeCastingCost = 5;
    @Entry
    public static int dreamWarpCastingCost = 15;
    @Entry
    public static int magicMissileCastingCost = 3;
    @Entry
    public static int telekinesisCastingCost = 4;
    @Entry
    public static int healCastingCost = 10;
    @Entry
    public static int discombobulateCastingCost = 10;
    @Entry
    public static int solarStrikeCastingCost = 20;
    @Entry
    public static int arcaneBarrierCastingCost = 4;

    @Entry
    public static List<String> excludeStructuresWithBookshelves = List.of();
    @Entry
    public static List<String> excludeStructuresWithLecterns = List.of();
}

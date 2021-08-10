package dev.cammiescorner.arcanus.core.config;

import dev.cammiescorner.arcanus.Arcanus;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Arcanus.MOD_ID)
public class ArcanusConfig implements ConfigData {
	public boolean haveBurnout = true;
	public boolean orangeSolarStrike = true;
	public int manaCooldown = 20;
	public int burnoutCooldown = 60;
	public int lungeCastingCost = 5;
	public int dreamWarpCastingCost = 15;
	public int magicMissileCastingCost = 3;
	public int telekinesisCastingCost = 4;
	public int healCastingCost = 10;
	public int meteorCastingCost = 6;
	public int solarStrikeCastingCost = 20;
	public int arcaneWallCastingCost = 2;
}

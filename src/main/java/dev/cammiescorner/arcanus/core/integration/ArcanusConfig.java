package dev.cammiescorner.arcanus.core.integration;

import dev.cammiescorner.arcanus.Arcanus;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Arcanus.MOD_ID)
public class ArcanusConfig implements ConfigData {
	@Comment("Should the Lectern Structure Processor run?")
	public boolean doLecternProcessor = true;

	@Comment("Should the Bookshelf Structure Processor run?")
	public boolean doBookshelfProcessor = true;

	@Comment("Should Village Libraries have Spell Books spawn?")
	public boolean librariesHaveBooks = true;

	@Comment("Should Stronghold Libraries have Spell Books spawn?")
	public boolean strongholdsHaveBooks = true;

	@Comment("Should Ruined Portals have Spell Books spawn?")
	public boolean ruinedPortalsHaveBooks = true;

	@Comment("Whether or not burnout should be applied when using too" +
			"\n    much mana.")
	public boolean haveBurnout = true;

	@Comment("The colour of objects created by spells (i.e. the laser" +
			"\n    from Solar Strike and the wall from Arcane Wall).")
	public String magicColour = Integer.toString(0x7ecdfb, 16);

	@Comment("The time Mana takes to refill by 1 in ticks before modifiers.")
	public int baseManaCooldown = 20;

	@Comment("The time Burnout takes to reduce by 1 in ticks before modifiers.")
	public int baseBurnoutCooldown = 60;

	@Comment("The Mana cost for the Lunge spell.")
	public int lungeCastingCost = 5;

	@Comment("The Mana cost for the Dream Warp spell.")
	public int dreamWarpCastingCost = 15;

	@Comment("The Mana cost for the Magic Missile spell.")
	public int magicMissileCastingCost = 3;

	@Comment("The Mana cost for the Telekinetic Shock spell.")
	public int telekinesisCastingCost = 4;

	@Comment("The Mana cost for the Heal spell.")
	public int healCastingCost = 10;

	@Comment("The Mana cost for the Discombobulate spell.")
	public int discombobulateCastingCost = 10;

	@Comment("The Mana cost for the Solar Strike spell.")
	public int solarStrikeCastingCost = 20;

	@Comment("The Mana cost for the Arcane Barrier spell.")
	public int arcaneBarrierCastingCost = 4;
}

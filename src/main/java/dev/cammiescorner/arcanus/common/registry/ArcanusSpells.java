package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.spells.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ArcanusSpells {
	//-----Spell Map-----//
	public static final LinkedHashMap<Spell, Identifier> SPELLS = new LinkedHashMap<>();

	public static final Spell EMPTY = create("empty", Spell.EMPTY);
	public static final Spell FULL_COWL = create("full_cowl", new FullCowlSpell());
	public static final Spell DISCHARGE = create("discharge", new DischargeSpell());
	public static final Spell MASTER_SPARK = create("master_spark", new MasterSparkSpell());
	public static final Spell BLACK_HOLE = create("black_hole", new BlackHoleSpell());
	public static final Spell ANIMATE_ARMOUR = create("animate_armor", new AnimateArmourSpell());
	public static final Spell TEMPORAL_DISRUPTION = create("temporal_disruption", new TemporalDisruptionSpell());

	//-----Registry-----//
	public static void register() {
		SPELLS.forEach((spell, id) -> Registry.register(Arcanus.SPELL, id, spell));
	}

	private static <T extends Spell> T create(String name, T spell) {
		SPELLS.put(spell, new Identifier(Arcanus.MOD_ID, name));
		return spell;
	}
}

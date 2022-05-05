package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;

public class DischargeSpell extends Spell {
	public DischargeSpell() {
		super(AuraType.TRANSMUTER, SpellComplexity.INTRICATE, 0, false);
	}
}

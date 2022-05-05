package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;

public class FullCowlSpell extends Spell {
	public FullCowlSpell() {
		super(AuraType.ENHANCER, SpellComplexity.COMPLEX, 200, true);
	}
}

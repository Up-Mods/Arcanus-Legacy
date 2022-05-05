package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;

public class BlackHoleSpell extends Spell {
	public BlackHoleSpell() {
		super(AuraType.CONJURER, SpellComplexity.COMPLEX, 200, true);
	}
}

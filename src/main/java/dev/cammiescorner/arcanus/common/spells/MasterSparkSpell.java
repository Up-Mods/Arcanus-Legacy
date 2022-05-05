package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;

public class MasterSparkSpell extends Spell {
	public MasterSparkSpell() {
		super(AuraType.EMITTER, SpellComplexity.COMPLEX, 0, false);
	}
}

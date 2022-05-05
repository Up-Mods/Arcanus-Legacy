package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.api.spells.SpellComplexity;

public class AnimateArmourSpell extends Spell {
	public AnimateArmourSpell() {
		super(AuraType.MANIPULATOR, SpellComplexity.COMPLEX, 200, true);
	}
}

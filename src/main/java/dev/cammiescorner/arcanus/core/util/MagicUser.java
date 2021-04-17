package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.common.spells.Spell;
import net.minecraft.util.Identifier;

import java.util.List;

public interface MagicUser
{
	List<Spell> getKnownSpells();
	void setKnownSpell(Identifier spellId);

	int getMana();
	int getMaxMana();
	void setMana(int amount);
	void addMana(int amount);

	void setLastCastTime(long time);
}

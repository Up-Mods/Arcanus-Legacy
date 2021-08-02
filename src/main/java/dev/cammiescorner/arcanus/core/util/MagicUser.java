package dev.cammiescorner.arcanus.core.util;

import net.minecraft.util.Identifier;

import java.util.List;

public interface MagicUser {
	List<Spell> getKnownSpells();

	void setKnownSpell(Identifier spellId);

	int getMana();

	int getMaxMana();

	void setMana(int amount);

	void addMana(int amount);

	int getBurnout();

	int getMaxBurnout();

	void setBurnout(int amount);

	void addBurnout(int amount);

	void setLastCastTime(long lastCastTime);

	void setActiveSpell(Spell spell, int timer);
}

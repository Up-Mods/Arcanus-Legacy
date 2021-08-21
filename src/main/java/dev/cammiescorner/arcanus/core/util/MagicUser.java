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

	float getManaRechargeRate();

	void setManaRechargeRate(float rate);

	void resetManaRechargeRate();

	int getBurnout();

	int getMaxBurnout();

	void setBurnout(int amount);

	void addBurnout(int amount);

	float getBurnoutRechargeRate();

	void setBurnoutRechargeRage(float rate);

	void resetBurnoutRechargeRate();

	int getManaLock();

	void setManaLock(int amount);

	void removeManaLock();

	boolean isManaVisible();

	void shouldShowMana(boolean shouldShowMana);

	void setLastCastTime(long lastCastTime);

	void setActiveSpell(Spell spell, int timer);
}

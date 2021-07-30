package dev.cammiescorner.arcanus.core.util;

import java.util.List;
import net.minecraft.resources.ResourceLocation;

public interface MagicUser
{
	List<Spell> getKnownSpells();
	void setKnownSpell(ResourceLocation spellId);

	int getMana();
	int getMaxMana();
	void setMana(int amount);
	void addMana(int amount);

	int getBurnout();
	int getMaxBurnout();
	void setBurnout(int amount);
	void addBurnout(int amount);

	void setLastCastTime(long lastCastTime);
}

package dev.cammiescorner.arcanus.entity;

import dev.cammiescorner.arcanus.spell.Spell;
import net.minecraft.util.Identifier;

import java.util.List;

public interface MagicUser {
    List<Spell> getKnownSpells();

    void setKnownSpell(Identifier spellId);

    int getMana();

    void setMana(int amount);

    int getMaxMana();

    void addMana(int amount);

    int getBurnout();

    void setBurnout(int amount);

    int getMaxBurnout();

    void addBurnout(int amount);

    boolean isManaVisible();

    void shouldShowMana(boolean shouldShowMana);

    void setLastCastTime(long lastCastTime);

    void setActiveSpell(Spell spell, int timer);
}

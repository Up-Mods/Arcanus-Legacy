package dev.cammiescorner.arcanus.entity;

import dev.cammiescorner.arcanus.spell.Spell;

public interface MagicUser {

    void setActiveSpell(Spell spell, int timer);
}

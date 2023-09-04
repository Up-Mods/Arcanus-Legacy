package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.spell.Spell;

import java.util.List;

//TODO make component
@Deprecated
public interface ClientUtils {
    List<Spell.Pattern> getPattern();

    void setTimer(int value);

    void setUnfinishedSpell(boolean value);
}

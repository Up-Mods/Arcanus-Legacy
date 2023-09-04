package dev.cammiescorner.arcanus.component.base;

import dev.cammiescorner.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface SpellMemory extends Component {

    Set<Spell> getKnownSpells();

    default boolean hasSpell(Spell spell) {
        return getKnownSpells().contains(spell);
    }

    boolean unlockSpell(@Nullable Spell spell);

    boolean removeSpell(@Nullable Spell spell);

    void clear();
}

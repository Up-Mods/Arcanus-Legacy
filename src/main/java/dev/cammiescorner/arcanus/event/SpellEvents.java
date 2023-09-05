package dev.cammiescorner.arcanus.event;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.spell.Spell;
import org.quiltmc.qsl.base.api.event.Event;
import org.quiltmc.qsl.base.api.util.TriState;

public class SpellEvents {

    public static final Event<TryCast> TRY_CAST = Event.create(TryCast.class, (listeners) -> (caster, spell) -> {
        for (TryCast listener : listeners) {
            TriState result = listener.tryCast(caster, spell);

            if (result != TriState.DEFAULT) {
                return result;
            }
        }

        return TriState.DEFAULT;
    });

    public static final Event<OnCast> ON_CAST = Event.create(OnCast.class, (listeners) -> (caster, spell) -> {
        for (OnCast listener : listeners) {
            listener.onCast(caster, spell);
        }
    });

    @FunctionalInterface
    public interface OnCast {

        void onCast(MagicCaster caster, Spell spell);
    }

    @FunctionalInterface
    public interface TryCast {

        TriState tryCast(MagicCaster caster, Spell spell);
    }
}

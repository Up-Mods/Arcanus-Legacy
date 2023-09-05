package dev.cammiescorner.arcanus.component.base;

import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface MagicCaster extends Component {

    int getMana();

    void setMana(int amount);

    default int getMaxMana() {
        return getMaxManaDirect();
    }

    /**
     * get the max mana ignoring mana lock
     */
    default int getMaxManaDirect() {
        return Math.max(ArcanusConfig.maxMana, 0);
    }

    default void addMana(int amount) {
        setMana(getMana() + amount);
    }

    default void drainMana(int amount) {
        addMana(-amount);
    }

    int getBurnout();

    void setBurnout(int amount);

    int getMaxBurnout();

    default void addBurnout(int amount) {
        setBurnout(getBurnout() + amount);
    }

    long getLastCastTime();

    void setLastCastTime(long lastCastTime);

    LivingEntity asEntity();

    boolean cast(Spell spell);

    void setActiveSpellTime(@Nullable Spell spell, int remainingTicks);

    default void clearActiveSpell() {
        setActiveSpellTime(null, 0);
    }

    Spell getActiveSpell();
}

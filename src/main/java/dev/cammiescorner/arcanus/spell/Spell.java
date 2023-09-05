package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import net.minecraft.Util;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class Spell {
    private final List<Pattern> spellPattern = new ArrayList<>(3);
    private final int manaCost;
    private String translationKey;

    public Spell(Pattern first, Pattern second, Pattern last, int manaCost) {
        this.spellPattern.add(first);
        this.spellPattern.add(second);
        this.spellPattern.add(last);
        this.manaCost = manaCost;
    }

    public int getMaxSpellTime() {
        return 0;
    }

    public List<Pattern> getSpellPattern() {
        return spellPattern;
    }

    public void onCast(MagicCaster caster) {
    }

    /**
     * only fired if {@link #getMaxSpellTime()} returns a value greater than 0
     */
    public void onActiveTick(Level world, MagicCaster caster, int remainingTicks) { }

    public int getManaCost() {
        return manaCost;
    }

    protected String getOrCreateTranslationKey() {
        if (this.translationKey == null)
            this.translationKey = Util.makeDescriptionId("spell", Arcanus.SPELL.getKey(this));

        return this.translationKey;
    }

    public String getTranslationKey() {
        return getOrCreateTranslationKey();
    }

    public enum Pattern {
        LEFT("L"), RIGHT("R");

        private final String symbol;

        Pattern(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}

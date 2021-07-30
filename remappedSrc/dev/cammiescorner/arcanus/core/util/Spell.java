package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Spell
{
	private List<Pattern> spellPattern = new ArrayList<>(3);
	private int manaCost;
	private String translationKey;

	public Spell(Pattern first, Pattern second, Pattern last, int manaCost)
	{
		this.spellPattern.add(first);
		this.spellPattern.add(second);
		this.spellPattern.add(last);
		this.manaCost = manaCost;
	}

	public List<Pattern> getSpellPattern()
	{
		return spellPattern;
	}

	public void onCast(Level world, Player player)
	{
		// player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 10 * 20, 0));
	}

	public int getManaCost()
	{
		return manaCost;
	}

	protected String getOrCreateTranslationKey()
	{
		if(this.translationKey == null)
			this.translationKey = Util.makeDescriptionId("spell", Arcanus.SPELL.getKey(this));

		return this.translationKey;
	}

	public String getTranslationKey()
	{
		return getOrCreateTranslationKey();
	}
}

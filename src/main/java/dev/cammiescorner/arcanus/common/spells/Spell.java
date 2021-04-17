package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.Pattern;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Spell
{
	private List<Pattern> spellPattern = new ArrayList<>(3);
	private int manaCost = 0;
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

	public void onCast(World world, PlayerEntity player)
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
			this.translationKey = Util.createTranslationKey("spell", Arcanus.SPELL.getId(this));

		return this.translationKey;
	}

	public String getTranslationKey()
	{
		return getOrCreateTranslationKey();
	}
}

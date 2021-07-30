package dev.cammiescorner.arcanus.common.spells;

import dev.cammiescorner.arcanus.core.util.Pattern;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MeteorSpell extends Spell
{
	public MeteorSpell(Pattern first, Pattern second, Pattern last, int manaCost)
	{
		super(first, second, last, manaCost);
	}

	@Override
	public void onCast(Level world, Player player)
	{
		super.onCast(world, player);
	}
}

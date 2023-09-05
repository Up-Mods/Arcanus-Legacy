package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.entity.MagicUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SolarStrikeSpell extends Spell {
    public SolarStrikeSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(Level world, Player player) {
        MagicUser user = (MagicUser) player;
        user.setActiveSpell(this, 0);
    }
}

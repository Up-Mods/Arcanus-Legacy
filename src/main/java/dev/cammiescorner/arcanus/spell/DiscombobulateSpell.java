package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.entity.MagicUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DiscombobulateSpell extends Spell {
    public DiscombobulateSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(World world, PlayerEntity player) {
        MagicUser user = (MagicUser) player;
        user.setActiveSpell(this, 0);
    }
}

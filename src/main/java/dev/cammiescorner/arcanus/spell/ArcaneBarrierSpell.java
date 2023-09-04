package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.entity.MagicUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ArcaneBarrierSpell extends Spell {
    public ArcaneBarrierSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(World world, PlayerEntity player) {
        MagicUser user = (MagicUser) player;
        user.setActiveSpell(this, 0);
    }
}
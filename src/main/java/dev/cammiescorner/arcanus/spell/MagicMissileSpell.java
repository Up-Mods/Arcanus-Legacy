package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class MagicMissileSpell extends Spell {

    public MagicMissileSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        MagicMissileEntity magicMissile = new MagicMissileEntity(entity, entity.getLevel());
        magicMissile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 4.5F, 0F);

        entity.getLevel().addFreshEntity(magicMissile);
        entity.getLevel().playSound(null, magicMissile, ArcanusSoundEvents.MAGIC_MISSILE, SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);
    }
}

package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class MagicMissileSpell extends Spell implements CanBeDisabled {

    public MagicMissileSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        MagicMissileEntity magicMissile = new MagicMissileEntity(entity, entity.level());
        magicMissile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 4.5F, 0F);

        entity.level().addFreshEntity(magicMissile);
        entity.level().playSound(null, magicMissile, ArcanusSoundEvents.MAGIC_MISSILE.get(), SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableMagicMissile;
    }
}

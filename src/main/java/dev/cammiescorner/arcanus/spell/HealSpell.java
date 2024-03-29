package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.registry.ArcanusParticles;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class HealSpell extends Spell implements CanBeDisabled {

    public HealSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        entity.heal(10);
        entity.level().playSound(null, entity, ArcanusSoundEvents.HEAL.get(), SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((entity.getRandom().nextInt(3) - 1) * entity.getRandom().nextFloat());
            float offsetY = entity.getRandom().nextFloat() * 2F;
            float offsetZ = ((entity.getRandom().nextInt(3) - 1) * entity.getRandom().nextFloat());

            ((ServerLevel) entity.level()).sendParticles(ArcanusParticles.HEAL.get(), entity.getX() + offsetX, entity.getY() - 0.5 + offsetY, entity.getZ() + offsetZ, 3, 0, 0, 0, 0);
        }
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableHeal;
    }
}

package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.registry.ArcanusParticles;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DiscombobulateSpell extends Spell {

    public DiscombobulateSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        HitResult result = ArcanusHelper.raycast(entity, 4F, true);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3 startPos = entity.getEyePosition(1F).add((entity.getRandom().nextInt(3) - 1) / startDivisor, (entity.getRandom().nextInt(3) - 1) / startDivisor, (entity.getRandom().nextInt(3) - 1) / startDivisor);
            Vec3 endPos = result.getLocation().add((entity.getRandom().nextInt(3) - 1) / endDivisor, (entity.getRandom().nextInt(3) - 1) / endDivisor, (entity.getRandom().nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, entity.getLevel(), 0.5F, (ParticleOptions) ArcanusParticles.DISCOMBOBULATE);
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) result).getEntity();
            ArcanusComponents.CAN_BE_DISCOMBOBULATED.maybeGet(target).ifPresent(component -> {
                component.setDiscombobulatedTimer(160);
                target.syncComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED);
            });
        } else if (entity instanceof Player player) {
            player.displayClientMessage(Arcanus.translate("spell", "no_target"), false);
        }
    }
}

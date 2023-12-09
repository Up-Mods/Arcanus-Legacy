package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ArcaneBarrierSpell extends Spell implements CanBeDisabled {

    public ArcaneBarrierSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        HitResult result = ArcanusHelper.raycast(entity, 24F, false);

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockResult = ((BlockHitResult) result);
            Direction side = blockResult.getDirection();
            BlockPos pos = blockResult.getBlockPos().offset(side.getStepX(), side.getStepY(), side.getStepZ());
            ArcaneBarrierEntity arcaneWall = new ArcaneBarrierEntity(entity.level());

            if (entity instanceof Player player) {
                arcaneWall.setOwner(player);

                if (!player.mayInteract(arcaneWall.level(), pos)) {
                    player.displayClientMessage(Arcanus.translate("spell", "cannot_interact"), false);
                    return;
                }
            }
            arcaneWall.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            entity.level().addFreshEntity(arcaneWall);
        } else {
            if (entity instanceof Player player) {
                player.displayClientMessage(Arcanus.translate("spell", "no_target"), false);
            }
        }
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableArcaneBarrier;
    }
}

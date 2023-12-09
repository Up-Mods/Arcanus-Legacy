package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.registry.ArcanusParticles;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

public class TelekinesisSpell extends Spell implements CanBeDisabled {

    public TelekinesisSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public void onCast(MagicCaster caster) {
        LivingEntity entity = caster.asEntity();
        HitResult result = ArcanusHelper.raycast(entity, 10F, true);
        Vec3 rotation = entity.getViewVector(1F);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3 startPos = entity.getEyePosition(1F).add((entity.getRandom().nextInt(3) - 1) / startDivisor, (entity.getRandom().nextInt(3) - 1) / startDivisor, (entity.getRandom().nextInt(3) - 1) / startDivisor);
            Vec3 endPos = result.getLocation().add((entity.getRandom().nextInt(3) - 1) / endDivisor, (entity.getRandom().nextInt(3) - 1) / endDivisor, (entity.getRandom().nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, entity.level(), 0.5F, ArcanusParticles.TELEKINETIC_SHOCK.get());
        }

        entity.level().playSound(null, entity, ArcanusSoundEvents.TELEKINETIC_SHOCK.get(), SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);

        switch (result.getType()) {
            case ENTITY -> {
                BlockPos pos = ((EntityHitResult) result).getEntity().blockPosition();
                if (entity instanceof Player player && !player.mayInteract(entity.level(), pos)) {
                    return;
                }

                entity.level().getEntities(entity, new AABB(pos), EntitySelector.ENTITY_STILL_ALIVE).forEach(target -> {
                    if (target instanceof AbstractArrow projectile)
                        projectile.startFalling();

                    target.setDeltaMovement(rotation.scale(2.5F));
                    target.hurtMarked = true;
                });
            }
            case BLOCK -> {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();
                if (entity instanceof Player player && !player.mayInteract(entity.level(), pos)) {
                    return;
                }

                BlockState state = entity.level().getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof TntBlock) {
                    TntBlock.explode(entity.level(), pos, entity);
                    entity.level().removeBlock(pos, false);

                    entity.level().getEntitiesOfClass(PrimedTnt.class, new AABB(pos), tnt -> tnt.isAlive() && tnt.getOwner() == entity).forEach(target -> {
                        target.setDeltaMovement(rotation.scale(2.5F));
                        target.hurtMarked = true;
                    });
                }

                if (block instanceof FallingBlock fallingBlock) {
                    FallingBlockEntity target = FallingBlockEntity.fall(entity.level(), pos, state);
                    fallingBlock.falling(target);
                    target.setDeltaMovement(rotation.scale(2.5F));
                    target.hurtMarked = true;
                    entity.level().addFreshEntity(target);
                }
            }
        }
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableTelekineticShock;
    }
}

package dev.cammiescorner.arcanus.spell;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.CanBeDisabled;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public class LungeSpell extends Spell implements CanBeDisabled {

    private final WeakHashMap<LivingEntity, Set<UUID>> affectedEntities = new WeakHashMap<>();

    public LungeSpell(Pattern first, Pattern second, Pattern last, int manaCost) {
        super(first, second, last, manaCost);
    }

    @Override
    public int getMaxSpellTime() {
        return 10;
    }

    @Override
    public void onActiveTick(Level level, MagicCaster caster, int remainingTicks) {
        LivingEntity entity = caster.asEntity();

        Set<UUID> hasHit = affectedEntities.computeIfAbsent(entity, e -> new HashSet<>());

        if (entity.isFallFlying()) {
            if (remainingTicks == 10)
                level.playSound(null, entity, SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);

            if (remainingTicks > 0) {
                Vec3 rotation = entity.getLookAngle();
                Vec3 velocity = entity.getDeltaMovement();
                float speed = 0.75F;

                entity.setDeltaMovement(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x), rotation.y * speed + (rotation.y * 1.5D - velocity.y), rotation.z * speed + (rotation.z * 1.5D - velocity.z)));

                level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(hitEntity -> {
                    if (hitEntity != entity && hitEntity instanceof LivingEntity && !hasHit.contains(hitEntity.getUUID())) {
                        if(entity instanceof Player player) {
                            hitEntity.hurt(entity.damageSources().playerAttack(player), 10);
                        }
                        else {
                            hitEntity.hurt(entity.damageSources().mobAttack(entity), 10);
                        }
                        hasHit.add(hitEntity.getUUID());
                    }
                });

                entity.hurtMarked = true;
            }

            if (entity.onGround() || remainingTicks == 0) {
                affectedEntities.remove(entity);
            }
        } else {
            if (remainingTicks == 10) {
                entity.setDeltaMovement(0F, 0.75F, 0F);
                level.playSound(null, entity, SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 2F, entity.getRandom().nextFloat() * 0.2F + 1.0F);
            }

            float adjustedPitch = Mth.abs(Mth.abs(entity.getXRot() / 90F) - 1);

            if (remainingTicks > 0) {
                entity.push((entity.getLookAngle().x * 0.025F + (entity.getLookAngle().x - entity.getDeltaMovement().x)) * adjustedPitch, 0F, (entity.getLookAngle().z * 0.025F + (entity.getLookAngle().z - entity.getDeltaMovement().z)) * adjustedPitch);
                level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach(hitEntity -> {
                    if (hitEntity != entity && hitEntity instanceof LivingEntity && !hasHit.contains(hitEntity.getUUID())) {
                        if (entity instanceof Player player) {
                            hitEntity.hurt(entity.damageSources().playerAttack(player), 10);
                        } else {
                            hitEntity.hurt(entity.damageSources().mobAttack(entity), 10);
                        }
                        hasHit.add(hitEntity.getUUID());
                    }
                });

                entity.hurtMarked = true;
            }

            entity.fallDistance = 0;

            if (entity.onGround() && remainingTicks <= 8) {
                level.explode(entity, entity.getX(), entity.getY() + 0.5, entity.getZ(), 1, Level.ExplosionInteraction.NONE);
                caster.clearActiveSpell();
                affectedEntities.remove(entity);
            }
        }
    }

    @Override
    public boolean enabled() {
        return ArcanusConfig.enableLunge;
    }
}

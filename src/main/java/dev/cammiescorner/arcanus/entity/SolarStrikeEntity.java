package dev.cammiescorner.arcanus.entity;

import dev.cammiescorner.arcanus.registry.ArcanusDamageTypes;
import dev.cammiescorner.arcanus.registry.ArcanusEntities;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class SolarStrikeEntity extends AbstractArrow {

    public final List<Entity> hasHit = new ArrayList<>();

    public SolarStrikeEntity(LivingEntity owner, Level world) {
        super(ArcanusEntities.SOLAR_STRIKE, owner, world);
    }

    public SolarStrikeEntity(EntityType<? extends SolarStrikeEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (tickCount <= 9) {
                AABB box = new AABB(getX() - 4, getY() - 1, getZ() - 4, getX() + 4, (level().getHeight() + 2048) - getY(), getZ() + 4);
                float radius = (float) (box.maxX - box.minX) / 2;

                level().getEntities(null, box).forEach(entity -> {
                    if (!hasHit.contains(entity)) {
                        Vec2 pos1 = new Vec2((float) getX(), (float) getZ());
                        Vec2 pos2 = new Vec2((float) entity.getX(), (float) entity.getZ());

                        if (entity instanceof LivingEntity || entity instanceof ArcaneBarrierEntity) {
                            if (entity instanceof LivingEntity)
                                entity.setSecondsOnFire(4);

                            entity.hurt(ArcanusDamageTypes.solarStrike(this, getOwner()), Math.max(10F, 50F * (1 - (Mth.sqrt(pos1.distanceToSqr(pos2)) / radius))));
                            entity.invulnerableTime = 0;
                            hasHit.add(entity);
                        }
                    }
                });
            }

            if (tickCount > 23)
                kill();
        } else {
            if (tickCount == 1)
                level().playLocalSound(getX(), getY(), getZ(), ArcanusSoundEvents.SOLAR_STRIKE, SoundSource.PLAYERS, Mth.clamp(1 - (Minecraft.getInstance().player.distanceTo(this) / 256F), 0, 1), (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);

            if (tickCount >= 2 && tickCount <= 5) {
                level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() + 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
                level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() - 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
                level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() + 2, 1.0D, 0.0D, 0.0D);
                level().addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() - 2, 1.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void kill() {
        if (!level().isClientSide())
            ((ServerLevel) level()).setChunkForced(chunkPosition().x, chunkPosition().z, false);

        super.kill();
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}

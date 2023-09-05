package dev.cammiescorner.arcanus.entity;

import dev.cammiescorner.arcanus.registry.ArcanusEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ArcaneBarrierEntity extends Entity {
    public static final float MAX_HEIGHT = 3F;
    public static final float GROWTH_RATE = 1F / 3F;
    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(ArcaneBarrierEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> HIT_TIMER = SynchedEntityData.defineId(ArcaneBarrierEntity.class, EntityDataSerializers.INT);
    private Player owner;

    public ArcaneBarrierEntity(Level world) {
        super(ArcanusEntities.ARCANE_BARRIER, world);
    }

    public ArcaneBarrierEntity(Level world, double x, double y, double z) {
        this(world);
        this.setPosRaw(x, y, z);
    }

    public ArcaneBarrierEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return !isRemoved();
    }

    @Override
    public void tick() {
        moveEntities();

        super.tick();

        if (!level.isClientSide()) {
            setHitTimer(Math.max(0, getHitTimer() - 1));

            if (tickCount > 0 && tickCount % 600 == 0)
                hurt(damageSources().generic(), 10);

            if ((getHealth() <= 0 && getHitTimer() <= 0))
                kill();
        }
    }

    // Top of bounding box collision breaks at y intervals of 16, starting at y=-1
    // (but not other y levels) if ArcaneBarrierEntity.MAX_HEIGHT is not a whole number????
    @Override
    protected AABB makeBoundingBox() {
        return super.makeBoundingBox().expandTowards(0F, Math.min(ArcaneBarrierEntity.MAX_HEIGHT, tickCount * ArcaneBarrierEntity.GROWTH_RATE), 0F);
    }

    private void moveEntities() {
        if (tickCount <= Math.ceil(ArcaneBarrierEntity.MAX_HEIGHT / ArcaneBarrierEntity.GROWTH_RATE)) {
            reapplyPosition();

            List<Entity> list = level.getEntities(this, getBoundingBox(), EntitySelector.NO_SPECTATORS.and((entity) -> !entity.isPassengerOfSameVehicle(this)));

            for (Entity entity : list)
                if (!(entity instanceof Shulker || entity instanceof ArcaneBarrierEntity) && !entity.noPhysics)
                    entity.move(MoverType.SHULKER, new Vec3(0, ArcaneBarrierEntity.GROWTH_RATE, 0));
        }
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(HEALTH, 40F);
        entityData.define(HIT_TIMER, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Owner"))
            owner = level.getPlayerByUUID(nbt.getUUID("Owner"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        if (owner != null)
            nbt.putUUID("Owner", owner.getUUID());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source))
            return false;
        else if (this.level.isClientSide)
            return false;
        else if (this.isRemoved())
            return false;

        if (getHitTimer() <= 0)
            setHealth(getHealth() - amount);

        setHitTimer(5);

        return true;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public float getHealth() {
        return entityData.get(HEALTH);
    }

    public void setHealth(float amount) {
        entityData.set(HEALTH, amount);
    }

    public int getHitTimer() {
        return entityData.get(HIT_TIMER);
    }

    public void setHitTimer(int amount) {
        entityData.set(HIT_TIMER, amount);
    }
}

package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ArcaneBarrierEntity extends Entity {
	private static final TrackedData<Float> HEALTH = DataTracker.registerData(ArcaneBarrierEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Integer> HIT_TIMER = DataTracker.registerData(ArcaneBarrierEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private PlayerEntity owner;
	public static final float MAX_HEIGHT = 3F;
	public static final float GROWTH_RATE = 1F / 3F;

	public ArcaneBarrierEntity(World world) {
		super(ModEntities.ARCANE_BARRIER, world);
	}

	public ArcaneBarrierEntity(PlayerEntity owner, World world) {
		this(world);
		this.owner = owner;
	}

	public ArcaneBarrierEntity(World world, double x, double y, double z) {
		this(world);
		this.setPos(x, y, z);
	}

	public ArcaneBarrierEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public boolean collidesWith(Entity other) {
		return true;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}

	@Override
	public boolean canHit() {
		return !isRemoved();
	}

	@Override
	public void tick() {
		moveEntities();

		super.tick();

		if(!world.isClient()) {
			setHitTimer(Math.max(0, getHitTimer() - 1));

			if(age > 0 && age % 600 == 0)
				damage(DamageSource.GENERIC, 10);

			if((getHealth() <= 0 && getHitTimer() <= 0))
				kill();
		}
	}

	// Top of bounding box collision breaks at y intervals of 16, starting at y=-1
	// (but not other y levels) if ArcaneBarrierEntity.MAX_HEIGHT is not a whole number????
	@Override
	protected Box calculateBoundingBox() {
		return super.calculateBoundingBox().stretch(0F, Math.min(ArcaneBarrierEntity.MAX_HEIGHT, age * ArcaneBarrierEntity.GROWTH_RATE), 0F);
	}

	private void moveEntities() {
		if(age <= Math.ceil(ArcaneBarrierEntity.MAX_HEIGHT / ArcaneBarrierEntity.GROWTH_RATE)) {
			refreshPosition();

			List<Entity> list = world.getOtherEntities(this, getBoundingBox(), EntityPredicates.EXCEPT_SPECTATOR.and((entity) -> !entity.isConnectedThroughVehicle(this)));

			for(Entity entity : list)
				if(!(entity instanceof ShulkerEntity || entity instanceof ArcaneBarrierEntity) && !entity.noClip)
					entity.move(MovementType.SHULKER, new Vec3d(0, ArcaneBarrierEntity.GROWTH_RATE, 0));
		}
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(HEALTH, 40F);
		dataTracker.startTracking(HIT_TIMER, 0);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if(nbt.contains("Owner"))
			owner = world.getPlayerByUuid(nbt.getUuid("Owner"));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		if(owner != null)
			nbt.putUuid("Owner", owner.getUuid());
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(this.isInvulnerableTo(source))
			return false;
		else if(this.world.isClient)
			return false;
		else if(this.isRemoved())
			return false;

		if(getHitTimer() <= 0)
			setHealth(getHealth() - amount);

		setHitTimer(5);

		return true;
	}

	public void setOwner(PlayerEntity owner) {
		this.owner = owner;
	}

	public float getHealth() {
		return dataTracker.get(HEALTH);
	}

	public void setHealth(float amount) {
		dataTracker.set(HEALTH, amount);
	}

	public int getHitTimer() {
		return dataTracker.get(HIT_TIMER);
	}

	public void setHitTimer(int amount) {
		dataTracker.set(HIT_TIMER, amount);
	}
}

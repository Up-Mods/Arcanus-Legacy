package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class ArcaneWallEntity extends Entity {
	private Entity owner;

	public ArcaneWallEntity(World world) {
		super(ModEntities.ARCANE_WALL, world);
	}

	public ArcaneWallEntity(LivingEntity owner, World world) {
		this(world);
		this.owner = owner;
	}

	public ArcaneWallEntity(World world, double x, double y, double z) {
		this(world);
		this.setPos(x, y, z);
	}

	public ArcaneWallEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {

	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {

	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this, owner == null ? 0 : owner.getId());
	}
}

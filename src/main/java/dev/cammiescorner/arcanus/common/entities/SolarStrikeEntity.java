package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModDamageSource;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public class SolarStrikeEntity extends PersistentProjectileEntity {
	public SolarStrikeEntity(LivingEntity owner, World world) {
		super(ModEntities.SOLAR_STRIKE, owner, world);
	}

	public SolarStrikeEntity(World world, double x, double y, double z) {
		super(ModEntities.SOLAR_STRIKE, x, y, z, world);
	}

	public SolarStrikeEntity(EntityType type, World world) {
		super(type, world);
	}

	@Override
	public void tick() {
		if(!world.isClient()) {
			if(age <= 9) {
				Box box = new Box(getX() - 4, getY() - 1, getZ() - 4, getX() + 4, (world.getHeight() + 2048) - getY(), getZ() + 4);
				float radius = (float) (box.maxX - box.minX) / 2;

				world.getOtherEntities(null, box).forEach(entity -> {
					Vec2f pos1 = new Vec2f((float) getX(), (float) getZ());
					Vec2f pos2 = new Vec2f((float) entity.getX(), (float) entity.getZ());

					if(entity instanceof LivingEntity || entity instanceof ArcaneBarrierEntity) {
						if(entity instanceof LivingEntity)
							entity.setOnFireFor(4);

						entity.damage(ModDamageSource.solarStrike(getOwner()), Math.max(10F, 50F * (1 - (MathHelper.sqrt(pos1.distanceSquared(pos2)) / radius))));
					}
				});
			}

			if(age > 23)
				kill();
		}
		else {
			if(age == 1)
				world.playSound(getX(), getY(), getZ(), ModSoundEvents.SOLAR_STRIKE, SoundCategory.PLAYERS, MathHelper.clamp(1 - (MinecraftClient.getInstance().player.distanceTo(this) / 256F), 0, 1), (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);

			if(age >= 2 && age <= 5) {
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() + 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() - 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() + 2, 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() - 2, 1.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void kill() {
		if(!world.isClient())
			((ServerWorld) world).setChunkForced(getChunkPos().x, getChunkPos().z, false);

		super.kill();
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	@Override
	public boolean shouldRender(double distance) {
		return true;
	}

	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}
}

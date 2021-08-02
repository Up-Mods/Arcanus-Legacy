package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public class SolarStrikeEntity extends PersistentProjectileEntity
{
	public SolarStrikeEntity(World world)
	{
		super(ModEntities.SOLAR_STRIKE, world);
	}

	@Override
	public void tick()
	{
		if(!world.isClient())
		{
			if(age <= 3)
			{
				if(age == 1)
					world.playSound(null, getBlockPos(), ModSoundEvents.SOLAR_STRIKE, SoundCategory.HOSTILE, 6F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);

				Box box = Box.of(getPos(), 8, (world.getHeight() + 64) - getY(), 8);
				float radius = (float) (box.maxX - box.minX) / 2;

				world.getOtherEntities(null, box).forEach(entity ->
				{
					Vec2f pos1 = new Vec2f((float) getX(), (float) getZ());
					Vec2f pos2 = new Vec2f((float) entity.getX(), (float) entity.getZ());

					if(entity instanceof LivingEntity)
					{
						entity.setOnFireFor(4);
						entity.damage(DamageSource.GENERIC, Math.max(10F, 40F * (1 - (MathHelper.sqrt(pos1.distanceSquared(pos2)) / radius))));
					}
				});
			}

			if(age > 23)
				kill();
		}
		else
		{
			if(age <= 3)
			{
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() + 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX() - 2, getY(), getZ(), 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() + 2, 1.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ() - 2, 1.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ)
	{
		return true;
	}

	@Override
	public boolean shouldRender(double distance)
	{
		return true;
	}

	@Override
	protected ItemStack asItemStack()
	{
		return ItemStack.EMPTY;
	}
}

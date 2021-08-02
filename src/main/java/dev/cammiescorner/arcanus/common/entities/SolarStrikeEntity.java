package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

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
				world.getOtherEntities(null, getBoundingBox().expand(age * 2)).forEach(entity ->
				{
					Vec2f pos1 = new Vec2f((float) getX(), (float) getZ());
					Vec2f pos2 = new Vec2f((float) entity.getX(), (float) entity.getZ());

					if(!isOwner(entity) && entity instanceof LivingEntity)
						entity.damage(DamageSource.DRAGON_BREATH, Math.max(7F, 50F * (1 - (MathHelper.sqrt(pos1.distanceSquared(pos2)) / 7F))));
				});

				world.createExplosion(this, getX(), getY() + 1, getZ(), 3F, Explosion.DestructionType.NONE);
			}

			if(age > 23)
				kill();
		}
	}

	@Override
	protected ItemStack asItemStack()
	{
		return ItemStack.EMPTY;
	}
}

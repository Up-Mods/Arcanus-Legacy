package dev.cammiescorner.arcanus.common.entities;

import dev.cammiescorner.arcanus.core.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class MagicMissileEntity extends PersistentProjectileEntity {
	public MagicMissileEntity(World world) {
		super(ModEntities.MAGIC_MISSILE, world);
		setDamage(5D);
	}

	public MagicMissileEntity(LivingEntity owner, World world) {
		super(ModEntities.MAGIC_MISSILE, owner, world);
		setDamage(5D);
	}

	public MagicMissileEntity(EntityType type, World world) {
		super(type, world);
		setDamage(5D);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		kill();
	}

	@Override
	protected ItemStack asItemStack() {
		return null;
	}
}

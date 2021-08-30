package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class ArcanusHelper {
	public static HitResult raycast(Entity origin, double maxDistance, boolean hitsEntities) {
		Vec3d startPos = origin.getCameraPosVec(1F);
		Vec3d rotation = origin.getRotationVec(1F);
		Vec3d endPos = startPos.add(rotation.x * maxDistance, rotation.y * maxDistance, rotation.z * maxDistance);
		HitResult hitResult = origin.world.raycast(new RaycastContext(startPos, endPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, origin));

		if(hitResult.getType() != HitResult.Type.MISS)
			endPos = hitResult.getPos();

		maxDistance *= 5;
		HitResult entityHitResult = ProjectileUtil.raycast(origin, startPos, endPos, origin.getBoundingBox().stretch(rotation.multiply(maxDistance)).expand(1.0D, 1.0D, 1.0D), entity -> !entity.isSpectator(), maxDistance);

		if(hitsEntities && entityHitResult != null)
			hitResult = entityHitResult;

		return hitResult;
	}

	public static void drawLine(Vec3d start, Vec3d end, World world, double density, ParticleEffect particle) {
		double totalDistance = start.distanceTo(end);

		for(double distanceTraveled = 0; distanceTraveled < totalDistance; distanceTraveled += density) {
			double alpha = distanceTraveled / totalDistance;
			double x = interpolate(start.x, end.x, alpha);
			double y = interpolate(start.y, end.y, alpha);
			double z = interpolate(start.z, end.z, alpha);

			if(world.isClient())
				world.addParticle(particle, x, y, z, 0, 0, 0);
			else
				((ServerWorld) world).spawnParticles(particle, x, y, z, 1, 0, 0, 0, 0);
		}
	}

	private static double interpolate(double start, double end, double alpha) {
		return start + (end - start) * alpha;
	}

	public static void addWandsToTab(List<ItemStack> list) {
		ItemStack stack = new ItemStack(ModItems.INITIATE_WAND);
		list.add(stack);

		stack = new ItemStack(ModItems.ADEPT_WAND);
		NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
		tag.putInt("Exp", 3200);
		list.add(stack);

		stack = new ItemStack(ModItems.MASTER_WAND);
		tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
		tag.putInt("Exp", 6400);
		list.add(stack);
	}
}

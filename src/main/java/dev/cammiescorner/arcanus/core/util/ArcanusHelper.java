package dev.cammiescorner.arcanus.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

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
}

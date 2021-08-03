package dev.cammiescorner.arcanus.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ArcanusHelper {
	public static HitResult raycast(Entity entity, double maxDistance) {
		Vec3d vec3d = entity.getCameraPosVec(1F);
		Vec3d vec3d2 = entity.getRotationVec(1F);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
		return entity.world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
	}
}

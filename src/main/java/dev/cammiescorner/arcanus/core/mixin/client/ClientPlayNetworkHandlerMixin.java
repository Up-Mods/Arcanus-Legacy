package dev.cammiescorner.arcanus.core.mixin.client;

import dev.cammiescorner.arcanus.common.entities.ArcaneWallEntity;
import dev.cammiescorner.arcanus.common.entities.MagicMissileEntity;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;

	@Inject(method = "onEntitySpawn", at = @At("TAIL"))
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo) {
		EntityType<?> type = packet.getEntityTypeId();
		double x = packet.getX();
		double y = packet.getY();
		double z = packet.getZ();
		Entity entity = null;

		if(type == ModEntities.SOLAR_STRIKE)
			entity = new SolarStrikeEntity(world, x, y, z);
		if(type == ModEntities.ARCANE_WALL)
			entity = new ArcaneWallEntity(world, x, y, z);
		if(type == ModEntities.MAGIC_MISSILE)
			entity = new MagicMissileEntity(world, x, y, z);

		if(entity != null) {
			if(entity instanceof PersistentProjectileEntity projectile)
				projectile.setOwner(world.getEntityById(packet.getEntityData()));
			if(entity instanceof ArcaneWallEntity wall)
				wall.setOwner(world.getEntityById(packet.getEntityData()));

			int id = packet.getId();
			entity.updateTrackedPosition(x, y, z);
			entity.refreshPositionAfterTeleport(x, y, z);
			entity.setPitch((packet.getPitch() * 360F) / 256F);
			entity.setYaw((packet.getYaw() * 360F) / 256F);
			entity.setId(id);
			entity.setUuid(packet.getUuid());
			world.addEntity(id, entity);
		}
	}
}

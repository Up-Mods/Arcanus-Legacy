package dev.cammiescorner.arcanus.api.actions;

import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class SummonAltarAction implements AltarAction {
	private Entity entity;
	private EntityType<?> type;

	@Override
	public void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
		if(entity == null)
			entity = type.create(world);

		if(entity != null) {
			entity.setPos(altar.getPos().getX() + 0.5, altar.getPos().getY() + 0.5, altar.getPos().getZ() + 0.5);
			world.spawnEntity(entity);
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntityType(EntityType<?> type) {
		this.type = type;
	}
}

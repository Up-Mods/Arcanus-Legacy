package dev.cammiescorner.arcanus.api.actions;

import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public interface AltarAction {
	AltarAction EMPTY = (world, player, altar) -> {};

	void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar);

	default boolean requiresPlayer() {
		return false;
	}
}

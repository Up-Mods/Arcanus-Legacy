package dev.cammiescorner.arcanus.api.actions;

import dev.cammiescorner.arcanus.api.TriConsumer;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface ConfiguredAltarAction {
	void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar);
	void write(PacketByteBuf buf);

	AltarAction getType();

	static ConfiguredAltarAction of(TriConsumer<ServerWorld, @Nullable ServerPlayerEntity, AmethystAltarBlockEntity> consumer, Consumer<PacketByteBuf> serializer, AltarAction type) {
		return new ConfiguredAltarAction() {
			@Override
			public void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
				consumer.accept(world, player, altar);
			}

			@Override
			public void write(PacketByteBuf buf) {
				serializer.accept(buf);
			}

			@Override
			public AltarAction getType() {
				return type;
			}
		};
	}

	@Environment(EnvType.CLIENT)
	static ConfiguredAltarAction ofClient(TriConsumer<ServerWorld, @Nullable ServerPlayerEntity, AmethystAltarBlockEntity> consumer, AltarAction type) {
		return new ConfiguredAltarAction() {
			@Override
			public void run(ServerWorld world, @Nullable ServerPlayerEntity player, AmethystAltarBlockEntity altar) {
				consumer.accept(world, player, altar);
			}

			@Override
			public void write(PacketByteBuf buf) {
				throw new UnsupportedOperationException();
			}

			@Override
			public AltarAction getType() {
				return type;
			}
		};
	}
}

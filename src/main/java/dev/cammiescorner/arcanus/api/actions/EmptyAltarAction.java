package dev.cammiescorner.arcanus.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.PacketByteBuf;

public class EmptyAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		return ConfiguredAltarAction.of((world, player, altar) -> {}, buf -> {}, this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		return ConfiguredAltarAction.ofClient((world, player, altar) -> {}, this);
	}
}

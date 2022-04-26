package dev.cammiescorner.arcanus.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import net.minecraft.network.PacketByteBuf;

public class RestoreAuraAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		return ConfiguredAltarAction.of((world, player, altar) -> ArcanusHelper.setAura(player, 20), buf -> {}, this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		return ConfiguredAltarAction.ofClient((world, player, altar) -> ArcanusHelper.setAura(player, 20), this);
	}

	@Override
	public boolean requiresPlayer() {
		return true;
	}
}

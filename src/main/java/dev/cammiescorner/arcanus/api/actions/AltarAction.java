package dev.cammiescorner.arcanus.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;

public abstract class AltarAction {
	public abstract ConfiguredAltarAction create(JsonObject json) throws JsonParseException;

	@Environment(EnvType.CLIENT)
	public abstract ConfiguredAltarAction create(PacketByteBuf buf);

	public boolean requiresPlayer() {
		return false;
	}
}

package dev.cammiescorner.arcanus.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class SummonAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		String string = JsonHelper.getString(json, "entity");
		EntityType<?> type = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Invalid Entity Type " + string + "!"));

		return ConfiguredAltarAction.of((world, player, altar) -> {
			Entity entity = type.create(world);
			entity.setPos(altar.getPos().getX() + 0.5, altar.getPos().getY() + 0.5, altar.getPos().getZ() + 0.5);
			world.spawnEntity(entity);
		}, buf -> buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(type)), this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		int entityId = buf.readVarInt();
		EntityType<?> type = Registry.ENTITY_TYPE.get(entityId);

		return ConfiguredAltarAction.ofClient((world, player, altar) -> {
			Entity entity = type.create(world);
			entity.setPos(altar.getPos().getX() + 0.5, altar.getPos().getY() + 0.5, altar.getPos().getZ() + 0.5);
			world.spawnEntity(entity);
		}, this);
	}
}

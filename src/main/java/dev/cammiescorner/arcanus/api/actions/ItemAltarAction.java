package dev.cammiescorner.arcanus.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;

public class ItemAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		int count = JsonHelper.getInt(json, "count", 1);
		Item item = ShapedRecipe.getItem(json);
		ItemStack stack = new ItemStack(item, count);

		if(json.has("data")) {

		}

		return ConfiguredAltarAction.of((world, player, altar) -> {
			ItemEntity entity = new ItemEntity(world, altar.getPos().getX() + 0.5, altar.getPos().getY() + 1.9, altar.getPos().getZ() + 0.5, stack.copy());
			entity.setNoGravity(true);
			entity.setVelocity(Vec3d.ZERO);
			world.spawnEntity(entity);
		}, buf -> buf.writeItemStack(stack), this);
	}

	@Override
	public ConfiguredAltarAction create(PacketByteBuf buf) {
		ItemStack stack = buf.readItemStack();

		return ConfiguredAltarAction.ofClient((world, player, altar) -> {
			if(!stack.isEmpty()) {
				ItemEntity entity = new ItemEntity(world, altar.getPos().getX() + 0.5, altar.getPos().getY() + 1.9, altar.getPos().getZ() + 0.5, stack.copy());
				entity.setNoGravity(true);
				entity.setVelocity(Vec3d.ZERO);
				world.spawnEntity(entity);
			}
		}, this);
	}
}

package dev.cammiescorner.arcanus.client.renderer.blocks;

import dev.cammiescorner.arcanus.client.AuraVertexConsumerProvider;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class AmethystAltarBlockEntityRenderer implements BlockEntityRenderer<AmethystAltarBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

	public AmethystAltarBlockEntityRenderer(BlockEntityRendererFactory.Context cxt) {

	}

	@Override
	public void render(AmethystAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();

		if(world != null) {
			double radius = 0.9 + Math.sin(world.getTime() * 0.1) * 0.2;
			DefaultedList<ItemStack> inventory = entity.getInventory();

			matrices.push();
			matrices.translate(0.5, 1, 0.5);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(world.getTime() * 2F));
			matrices.translate(-(radius * 0.5), 0, -(radius * 1.54));

			for(int i = 0; i < inventory.size(); i++) {
				double angle = Math.toRadians(36 * i);
				double rotX = (Math.cos(angle) * radius);
				double rotZ = (Math.sin(angle) * radius);
				matrices.translate(rotX, 0, rotZ);

				ItemStack stack = inventory.get(i);

				matrices.push();
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(15));
				matrices.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion((float) angle));
				itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, new AuraVertexConsumerProvider(vertexConsumers, 255, 255, 255, 255), (int) entity.getPos().asLong());
				itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, (int) entity.getPos().asLong());
				matrices.pop();
			}

			matrices.pop();
		}
	}
}

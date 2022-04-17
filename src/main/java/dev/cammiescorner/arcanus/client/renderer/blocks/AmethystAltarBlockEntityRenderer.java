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
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class AmethystAltarBlockEntityRenderer implements BlockEntityRenderer<AmethystAltarBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

	public AmethystAltarBlockEntityRenderer(BlockEntityRendererFactory.Context cxt) {

	}

	@Override
	public void render(AmethystAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = entity.getWorld();
		int filledSlots = entity.filledSlots();

		if(world != null && filledSlots > 0) {
			double radius = 1.25 + Math.sin(world.getTime() * 0.1) * 0.3;
			double angleBetween = 360 / (double) filledSlots;

			matrices.push();
			matrices.translate(0.5, 1, 0.5);
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(world.getTime() * 2F));

			for(int i = 0; i < filledSlots; ++i) {
				double angle = Math.toRadians(angleBetween * i);
				double rotX = Math.cos(angle) * radius;
				double rotZ = Math.sin(angle) * radius;
				ItemStack stack = entity.getStack(i);

				matrices.push();
				matrices.translate(rotX, 0, rotZ);
				matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90));
				matrices.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion((float) angle));
				itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, new AuraVertexConsumerProvider(vertexConsumers, 255, 255, 255, 255), (int) entity.getPos().asLong());
				itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, (int) entity.getPos().asLong());
				matrices.pop();
			}

			matrices.pop();
		}
	}
}

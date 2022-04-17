package dev.cammiescorner.arcanus.client.renderer.blocks;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.client.AuraVertexConsumerProvider;
import dev.cammiescorner.arcanus.common.blocks.entities.AmethystAltarBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class AmethystAltarBlockEntityRenderer implements BlockEntityRenderer<AmethystAltarBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	private final BlockRenderManager blockRenderer = MinecraftClient.getInstance().getBlockRenderManager();

	public AmethystAltarBlockEntityRenderer(BlockEntityRendererFactory.Context cxt) {

	}

	@Override
	public void render(AmethystAltarBlockEntity altar, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = altar.getWorld();
		int filledSlots = altar.filledSlots();

		if(world != null) {
			HashMap<BlockPos, BlockState> structureMap = ArcanusHelper.getStructureMap(world);

			if(structureMap != null && !structureMap.isEmpty() && !altar.isCompleted()) {
				float scale = 0.875F;
				float offset = 5 - ((1 - scale) * 0.5F);

				for(Map.Entry<BlockPos, BlockState> entry : structureMap.entrySet()) {
					BlockPos pos = entry.getKey();
					BlockState state = entry.getValue();

					matrices.push();
					matrices.translate(pos.getX() - offset, pos.getY() + ((1 - scale) * 0.5) - 0.0001, pos.getZ() - offset);
					matrices.scale(scale, scale, scale);
					blockRenderer.renderBlock(state, altar.getPos(), world, matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, world.getRandom());
					matrices.pop();
				}
			}

			if(filledSlots > 0) {
				double time = world.getTime() + tickDelta;
				double radius = 1.25 + Math.sin(time * 0.1) * 0.3;
				double angleBetween = 360 / (double) filledSlots;

				matrices.push();
				matrices.translate(0.5, 1, 0.5);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (time * 2F)));

				for(int i = 0; i < filledSlots; ++i) {
					ItemStack stack = altar.getStack(i);
					double angle = Math.toRadians(angleBetween * i);
					double rotX = Math.cos(angle) * radius;
					double rotZ = Math.sin(angle) * radius;

					matrices.push();
					matrices.translate(rotX, 0, rotZ);
					matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90));
					matrices.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion((float) angle));
					itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, new AuraVertexConsumerProvider(vertexConsumers, 255, 255, 255, 255), (int) altar.getPos().asLong());
					itemRenderer.renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, (int) altar.getPos().asLong());
					matrices.pop();
				}

				matrices.pop();
			}
		}
	}
}

package dev.cammiescorner.arcanus.client.renderer.blockentity;

import dev.cammiescorner.arcanus.common.blocks.DisplayCaseBlock;
import dev.cammiescorner.arcanus.common.blocks.entities.DisplayCaseBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class DisplayCaseBlockEntityRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity> {
	private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

	public DisplayCaseBlockEntityRenderer(BlockEntityRendererFactory.Context cxt) { }

	@Override
	public void render(DisplayCaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ItemStack stack = entity.getStack(0);

		if(stack.getCount() > 0) {
			Direction direction = entity.getCachedState().get(DisplayCaseBlock.FACING);
			float rotation = direction == Direction.NORTH ? 0 : direction == Direction.EAST ? 270 : direction == Direction.SOUTH ? 180 : 90;

					matrices.push();

			if(stack.getItem() instanceof BlockItem) {
				matrices.translate(0.5D, 0.9D, 0.5D);
				matrices.scale(0.35F, 0.35F, 0.35F);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
			}
			else {
				matrices.translate(0.5D, 0.84D, 0.5D);
				matrices.scale(0.7F, 0.7F, 0.7F);
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
			}

			itemRenderer.renderItem(stack, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, (int) entity.getPos().asLong());
			matrices.pop();
		}
	}
}

package dev.cammiescorner.arcanus.client.renderer.blockentity;

import dev.cammiescorner.arcanus.common.blocks.DisplayCaseBlock;
import dev.cammiescorner.arcanus.common.blocks.entities.DisplayCaseBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;

public class DisplayCaseBlockEntityRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity> {

    private final ItemRenderer itemRenderer;

    public DisplayCaseBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(DisplayCaseBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack stack = entity.getStack(0);

        if (stack.getCount() > 0) {
            Direction direction = entity.getCachedState().get(DisplayCaseBlock.FACING);
            float rotation = direction == Direction.NORTH ? 0 : direction == Direction.EAST ? 270 : direction == Direction.SOUTH ? 180 : 90;

            matrices.push();

            if (stack.getItem() instanceof BlockItem) {
                matrices.translate(0.5D, 0.9D, 0.5D);
                matrices.scale(0.35F, 0.35F, 0.35F);
                matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(rotation));
            } else {
                matrices.translate(0.5D, 0.84D, 0.5D);
                matrices.scale(0.7F, 0.7F, 0.7F);
                matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(rotation));
                matrices.multiply(Axis.X_POSITIVE.rotationDegrees(90));
            }

            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, entity.getWorld(), (int) entity.getPos().asLong());
            matrices.pop();
        }
    }
}

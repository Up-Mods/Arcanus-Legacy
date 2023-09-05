package dev.cammiescorner.arcanus.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.block.DisplayCaseBlock;
import dev.cammiescorner.arcanus.block.entity.DisplayCaseBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DisplayCaseBlockEntityRenderer implements BlockEntityRenderer<DisplayCaseBlockEntity> {

    private final ItemRenderer itemRenderer;

    public DisplayCaseBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(DisplayCaseBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemStack stack = entity.getItem(0);

        if (stack.getCount() > 0) {
            Direction direction = entity.getBlockState().getValue(DisplayCaseBlock.FACING);
            float rotation = direction == Direction.NORTH ? 0 : direction == Direction.EAST ? 270 : direction == Direction.SOUTH ? 180 : 90;

            matrices.pushPose();

            if (stack.getItem() instanceof BlockItem) {
                matrices.translate(0.5D, 0.9D, 0.5D);
                matrices.scale(0.35F, 0.35F, 0.35F);
                matrices.mulPose(Axis.YP.rotationDegrees(rotation));
            } else {
                matrices.translate(0.5D, 0.84D, 0.5D);
                matrices.scale(0.7F, 0.7F, 0.7F);
                matrices.mulPose(Axis.YP.rotationDegrees(rotation));
                matrices.mulPose(Axis.XP.rotationDegrees(90));
            }

            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, entity.getLevel(), (int) entity.getBlockPos().asLong());
            matrices.popPose();
        }
    }
}

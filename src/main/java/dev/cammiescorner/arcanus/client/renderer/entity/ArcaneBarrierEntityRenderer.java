package dev.cammiescorner.arcanus.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ArcaneBarrierEntityRenderer extends EntityRenderer<ArcaneBarrierEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/misc/white.png");

    public ArcaneBarrierEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ArcaneBarrierEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
        super.render(entity, yaw, tickDelta, matrices, provider, light);
        renderBeam(entity, 0, Math.min(ArcaneBarrierEntity.MAX_HEIGHT, (entity.tickCount + tickDelta) * ArcaneBarrierEntity.GROWTH_RATE), 0, tickDelta, matrices, provider, light);
    }

    public void renderBeam(ArcaneBarrierEntity entity, float x, float y, float z, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
        float squaredLength = x * x + y * y + z * z;
        float length = Mth.sqrt(squaredLength);
        final int maxQuads = 4;
        final float radius = 0.71F;
        float ageDelta = entity.tickCount + tickDelta;
        float hitTimerDelta = entity.getHitTimer() + tickDelta;
        float alpha;
        int colour = Integer.parseInt(ArcanusConfig.magicColour, 16);

        if (entity.getHitTimer() == 0)
            alpha = ageDelta < 5 ? (int) (ageDelta / 6F) : 1F;
        else
            alpha = (int) (hitTimerDelta / (5F + tickDelta));

        float[] rgb = ArcanusHelper.getRGBMultiply(colour, alpha);

        matrices.pushPose();

        VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.getMagicCircles(TEXTURE));
        PoseStack.Pose entry = matrices.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f normal = entry.normal();
        float f = 0.5025F;

        vertexConsumer.vertex(matrix4f, -f, length, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.UP.getNormal().getX(), Direction.UP.getNormal().getY(), Direction.UP.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, -f, length, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.UP.getNormal().getX(), Direction.UP.getNormal().getY(), Direction.UP.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, f, length, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.UP.getNormal().getX(), Direction.UP.getNormal().getY(), Direction.UP.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, f, length, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.UP.getNormal().getX(), Direction.UP.getNormal().getY(), Direction.UP.getNormal().getZ()).endVertex();

        vertexConsumer.vertex(matrix4f, -f, 0F, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.DOWN.getNormal().getX(), Direction.DOWN.getNormal().getY(), Direction.DOWN.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, f, 0F, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.DOWN.getNormal().getX(), Direction.DOWN.getNormal().getY(), Direction.DOWN.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, f, 0F, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.DOWN.getNormal().getX(), Direction.DOWN.getNormal().getY(), Direction.DOWN.getNormal().getZ()).endVertex();
        vertexConsumer.vertex(matrix4f, -f, 0F, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, Direction.DOWN.getNormal().getX(), Direction.DOWN.getNormal().getY(), Direction.DOWN.getNormal().getZ()).endVertex();

        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(45));
        entry = matrices.last();
        matrix4f = entry.pose();
        float vertX1 = 0F;
        float vertY1 = radius;

        for (int i = 1; i <= maxQuads; i++) {
            float vertX2 = Mth.sin(i * 6.2831855F / maxQuads) * radius;
            float vertY2 = Mth.cos(i * 6.2831855F / maxQuads) * radius;
            Direction direction = Direction.values()[i + 1];

            vertexConsumer.vertex(matrix4f, vertX1, 0F, vertY1).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
            vertexConsumer.vertex(matrix4f, vertX2, 0F, vertY2).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
            vertexConsumer.vertex(matrix4f, vertX2, length, vertY2).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();
            vertexConsumer.vertex(matrix4f, vertX1, length, vertY1).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ()).endVertex();

            vertX1 = vertX2;
            vertY1 = vertY2;
        }

        matrices.popPose();
        matrices.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ArcaneBarrierEntity entity) {
        return TEXTURE;
    }
}

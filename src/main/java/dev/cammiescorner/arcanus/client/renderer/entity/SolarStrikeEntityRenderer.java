package dev.cammiescorner.arcanus.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SolarStrikeEntityRenderer extends EntityRenderer<SolarStrikeEntity> {
    public SolarStrikeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(SolarStrikeEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light) {
        renderBeam(entity, matrices, provider, 0, (float) ((entity.level().getHeight() + 2048) - entity.getY()), 0, tickDelta, OverlayTexture.NO_OVERLAY, light);
    }

    public void renderBeam(SolarStrikeEntity entity, PoseStack matrices, MultiBufferSource provider, float x, float y, float z, float tickDelta, int overlay, int light) {
        float squaredLength = x * x + y * y + z * z;
        float length = Mth.sqrt(squaredLength);
        float ageDelta = (entity.tickCount - 1) + tickDelta;
        float scale = ageDelta < 3 ? (ageDelta) / 3F : ageDelta > 9 ? 1 - ((ageDelta - 9F) / 15F) : 1F;
        float alpha = ageDelta < 3 ? 1 : Mth.clamp(1 - ((ageDelta - 3) / 23F), 0, 1);
        int colour = Integer.parseInt(ArcanusConfig.magicColour, 16);

        final int maxQuads = 16;
        final float radius = 2.25F;

        float[] rgb = ArcanusHelper.getRGBMultiply(colour, alpha);

        matrices.pushPose();
        matrices.mulPose(Axis.XP.rotationDegrees(-90));
        matrices.scale(scale, scale, 1);

        VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.getMagicCircles(new ResourceLocation("textures/misc/white.png")));
        PoseStack.Pose entry = matrices.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = matrices.last().normal();
        float vertX1 = 0F;
        float vertY1 = radius;

        for (int i = 1; i <= maxQuads; i++) {
            float vertX2 = Mth.sin(i * 6.2831855F / maxQuads) * radius;
            float vertY2 = Mth.cos(i * 6.2831855F / maxQuads) * radius;
            Vector3f u = new Vector3f(vertX2 - vertX1, vertY2 - vertY1, length);
            Vector3f v = new Vector3f(vertX1 - vertX2, vertY1 - vertY2, -length);
            Vector3f normal = u.cross(v);

            vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlayCoords(overlay).uv2(light).normal(matrix3f, normal.x, normal.y, normal.z).endVertex();
            vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlayCoords(overlay).uv2(light).normal(matrix3f, normal.x, normal.y, normal.z).endVertex();
            vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlayCoords(overlay).uv2(light).normal(matrix3f, normal.x, normal.y, normal.z).endVertex();
            vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(matrix3f, normal.x, normal.y, normal.z).endVertex();

            vertX1 = vertX2;
            vertY1 = vertY2;
        }

        matrices.popPose();
    }

    @Override
    public boolean shouldRender(SolarStrikeEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(SolarStrikeEntity entity) {
        return null;
    }
}

package dev.cammiescorner.arcanus.client.renderer.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ArcaneBarrierEntityRenderer extends EntityRenderer<ArcaneBarrierEntity> {

    private static final Identifier TEXTURE = new Identifier("textures/misc/white.png");

    public ArcaneBarrierEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(ArcaneBarrierEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
        super.render(entity, yaw, tickDelta, matrices, provider, light);
        renderBeam(entity, 0, Math.min(ArcaneBarrierEntity.MAX_HEIGHT, (entity.age + tickDelta) * ArcaneBarrierEntity.GROWTH_RATE), 0, tickDelta, matrices, provider, light);
    }

    public void renderBeam(ArcaneBarrierEntity entity, float x, float y, float z, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
        float squaredLength = x * x + y * y + z * z;
        float length = MathHelper.sqrt(squaredLength);
        final int maxQuads = 4;
        final float radius = 0.71F;
        float ageDelta = entity.age + tickDelta;
        float hitTimerDelta = entity.getHitTimer() + tickDelta;
        float alpha;
        int colour = Integer.parseInt(ArcanusConfig.magicColour, 16);

        if (entity.getHitTimer() == 0)
            alpha = ageDelta < 5 ? (int) (ageDelta / 6F) : 1F;
        else
            alpha = (int) (hitTimerDelta / (5F + tickDelta));

        float[] rgb = ArcanusHelper.getRGBMultiply(colour, alpha);

        matrices.push();

        VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.getMagicCircles(TEXTURE));
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getModel();
        Matrix3f normal = entry.getNormal();
        float f = 0.5025F;

        vertexConsumer.vertex(matrix4f, -f, length, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.UP.getVector().getX(), Direction.UP.getVector().getY(), Direction.UP.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, -f, length, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.UP.getVector().getX(), Direction.UP.getVector().getY(), Direction.UP.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, f, length, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.UP.getVector().getX(), Direction.UP.getVector().getY(), Direction.UP.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, f, length, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.UP.getVector().getX(), Direction.UP.getVector().getY(), Direction.UP.getVector().getZ()).next();

        vertexConsumer.vertex(matrix4f, -f, 0F, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.DOWN.getVector().getX(), Direction.DOWN.getVector().getY(), Direction.DOWN.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, f, 0F, -f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.DOWN.getVector().getX(), Direction.DOWN.getVector().getY(), Direction.DOWN.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, f, 0F, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.DOWN.getVector().getX(), Direction.DOWN.getVector().getY(), Direction.DOWN.getVector().getZ()).next();
        vertexConsumer.vertex(matrix4f, -f, 0F, f).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, Direction.DOWN.getVector().getX(), Direction.DOWN.getVector().getY(), Direction.DOWN.getVector().getZ()).next();

        matrices.push();
        matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(45));
        entry = matrices.peek();
        matrix4f = entry.getModel();
        float vertX1 = 0F;
        float vertY1 = radius;

        for (int i = 1; i <= maxQuads; i++) {
            float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
            float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;
            Direction direction = Direction.values()[i + 1];

            vertexConsumer.vertex(matrix4f, vertX1, 0F, vertY1).color(rgb[0], rgb[1], rgb[2], 1F).uv(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
            vertexConsumer.vertex(matrix4f, vertX2, 0F, vertY2).color(rgb[0], rgb[1], rgb[2], 1F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
            vertexConsumer.vertex(matrix4f, vertX2, length, vertY2).color(rgb[0], rgb[1], rgb[2], 1F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();
            vertexConsumer.vertex(matrix4f, vertX1, length, vertY1).color(rgb[0], rgb[1], rgb[2], 1F).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normal, direction.getVector().getX(), direction.getVector().getY(), direction.getVector().getZ()).next();

            vertX1 = vertX2;
            vertY1 = vertY2;
        }

        matrices.pop();
        matrices.pop();
    }

    @Override
    public Identifier getTexture(ArcaneBarrierEntity entity) {
        return TEXTURE;
    }
}

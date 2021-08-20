package dev.cammiescorner.arcanus.client.renderer.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.common.entities.ArcaneWallEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class ArcaneWallEntityRenderer extends EntityRenderer<ArcaneWallEntity> {
	public ArcaneWallEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(ArcaneWallEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		super.render(entity, yaw, tickDelta, matrices, provider, light);
		renderBeam(entity, 0, entity.getHeight(), 0, tickDelta, matrices, provider, light);
	}

	public void renderBeam(ArcaneWallEntity entity, float x, float y, float z, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);

		final int maxQuads = 4;
		final float radius = 0.71F;
		float ageDelta = (entity.age - 1) + tickDelta;
		final int red = Integer.parseInt(Arcanus.config.magicColour, 16) >> 16;
		final int green = Integer.parseInt(Arcanus.config.magicColour, 16) >> 8;
		final int blue = Integer.parseInt(Arcanus.config.magicColour, 16);
		int alpha = ageDelta < 5 ? (int) (255 * (ageDelta / 6F)) : 255;

		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));

		VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.MAGIC);
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();
		float vertX1 = 0F;
		float vertY1 = radius;

		for(int i = 1; i <= maxQuads; i++) {
			float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
			float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;

			vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(red, green, blue, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(red, green, blue, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(red, green, blue, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(red, green, blue, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();

			vertX1 = vertX2;
			vertY1 = vertY2;
		}

		matrices.pop();
	}

	@Override
	public Identifier getTexture(ArcaneWallEntity entity) {
		return null;
	}
}

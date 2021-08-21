package dev.cammiescorner.arcanus.client.renderer.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.common.entities.ArcaneBarrierEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class ArcaneBarrierEntityRenderer extends EntityRenderer<ArcaneBarrierEntity> {
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
		final int red = Integer.parseInt(Arcanus.config.magicColour, 16) >> 16;
		final int green = Integer.parseInt(Arcanus.config.magicColour, 16) >> 8;
		final int blue = Integer.parseInt(Arcanus.config.magicColour, 16);
		int alpha;

		if(entity.getHitTimer() == 0)
			alpha = ageDelta < 5 ? (int) (255 * (ageDelta / 6F)) : 255;
		else
			alpha = (int) (255 * (hitTimerDelta / (5F + tickDelta)));

		matrices.push();

		VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.MAGIC);
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		float f = 0.5025F;

		vertexConsumer.vertex(matrix4f, -f, length, -f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, -f, length, f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, f, length, f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, f, length, -f).color(red, green, blue, alpha).next();

		vertexConsumer.vertex(matrix4f, -f, 0F, -f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, f, 0F, -f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, f, 0F, f).color(red, green, blue, alpha).next();
		vertexConsumer.vertex(matrix4f, -f, 0F, f).color(red, green, blue, alpha).next();

		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(45));
		entry = matrices.peek();
		matrix4f = entry.getModel();
		float vertX1 = 0F;
		float vertY1 = radius;

		for(int i = 1; i <= maxQuads; i++) {
			float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
			float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;

			vertexConsumer.vertex(matrix4f, vertX1, 0F, vertY1).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX2, 0F, vertY2).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX2, length, vertY2).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX1, length, vertY1).color(red, green, blue, alpha).next();

			vertX1 = vertX2;
			vertY1 = vertY2;
		}

		matrices.pop();
		matrices.pop();
	}

	@Override
	public Identifier getTexture(ArcaneBarrierEntity entity) {
		return null;
	}
}

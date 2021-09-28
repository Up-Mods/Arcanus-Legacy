package dev.cammiescorner.arcanus.client.renderer.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.minecraft.client.render.Frustum;
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

public class SolarStrikeEntityRenderer extends EntityRenderer<SolarStrikeEntity> {
	public SolarStrikeEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(SolarStrikeEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		renderBeam(0, (float) ((entity.world.getHeight() + 2048) - entity.getY()), 0, tickDelta, entity.age, matrices, provider, light);
	}

	public void renderBeam(float x, float y, float z, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);
		float ageDelta = (age - 1) + tickDelta;
		float scale = ageDelta < 3 ? (ageDelta) / 3F : ageDelta > 9 ? 1 - ((ageDelta - 9F) / 15F) : 1F;
		int alpha = ageDelta < 3 ? 255 : (int) (255 * (1 - ((ageDelta - 3) / 23F)));

		final int maxQuads = 16;
		final float radius = 2.25F;
		final int red = Integer.parseInt(Arcanus.getConfig().magicColour, 16) >> 16;
		final int green = Integer.parseInt(Arcanus.getConfig().magicColour, 16) >> 8;
		final int blue = Integer.parseInt(Arcanus.getConfig().magicColour, 16);

		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
		matrices.scale(scale, scale, 1);

		VertexConsumer vertexConsumer = provider.getBuffer(ArcanusClient.MAGIC);
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();
		float vertX1 = 0F;
		float vertY1 = radius;

		for(int i = 1; i <= maxQuads; i++) {
			float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
			float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;

			vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(red, green, blue, alpha).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(red, green, blue, alpha).next();

			vertX1 = vertX2;
			vertY1 = vertY2;
		}

		matrices.pop();
	}

	@Override
	public boolean shouldRender(SolarStrikeEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public Identifier getTexture(SolarStrikeEntity entity) {
		return null;
	}
}

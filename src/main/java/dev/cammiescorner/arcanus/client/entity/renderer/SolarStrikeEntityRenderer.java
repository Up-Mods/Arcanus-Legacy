package dev.cammiescorner.arcanus.client.entity.renderer;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
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
		renderBeam(0, (float) ((entity.world.getHeight() + 64) - entity.getY()), 0, tickDelta, entity.age, matrices, provider, light);
		super.render(entity, yaw, tickDelta, matrices, provider, light);
	}

	public void renderBeam(float x, float y, float z, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);
		float ageDelta = (age - 1) + tickDelta;
		float scale = ageDelta < 3 ? (ageDelta) / 3F : ageDelta > 9 ? 1 - ((ageDelta - 9F) / 15F) : 1F;
		int alpha = ageDelta < 3 ? 255 : (int) (255 * (1 - ((ageDelta - 3) / 23F)));

		final int maxQuads = 16;
		final float radius = 2.25F;
		final boolean orangeLaser = Arcanus.config.orangeSolarStrike;

		matrices.push();
		matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
		matrices.scale(scale, scale, 1);

		VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getLightning());
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();
		float vertX1 = 0F;
		float vertY1 = radius;

		for(int i = 1; i <= maxQuads; i++) {
			float vertX2 = MathHelper.sin(i * 6.2831855F / maxQuads) * radius;
			float vertY2 = MathHelper.cos(i * 6.2831855F / maxQuads) * radius;

			vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(orangeLaser ? 255 : 126, orangeLaser ? 187 : 205, orangeLaser ? 63 : 251, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(orangeLaser ? 255 : 126, orangeLaser ? 187 : 205, orangeLaser ? 63 : 251, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(orangeLaser ? 255 : 126, orangeLaser ? 187 : 205, orangeLaser ? 63 : 251, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(orangeLaser ? 255 : 126, orangeLaser ? 187 : 205, orangeLaser ? 63 : 251, alpha).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();

			vertX1 = vertX2;
			vertY1 = vertY2;
		}

		matrices.pop();
	}

	@Override
	public Identifier getTexture(SolarStrikeEntity entity) {
		return null;
	}
}

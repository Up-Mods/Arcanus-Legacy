package dev.cammiescorner.arcanus.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class AuraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final FeatureRenderer<T, M>[] otherFeatureRenderers;

    @SafeVarargs
    public AuraFeatureRenderer(FeatureRendererContext<T, M> context, FeatureRenderer<T, M>... otherFeatureRenderers) {
        super(context);
        this.otherFeatureRenderers = otherFeatureRenderers;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        float aura = AuraEffectManager.getAuraFor(entity);

        if (aura > 0) {
            this.renderAura(matrices, vertexConsumers, entity, light, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch, (int) (aura * 255), AuraEffectManager.getAuraColourFor(entity));
        }
    }

    private void renderAura(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, int light, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, int aura, int[] auraColour) {
        float scale = 1f;

        var auraConsumerProvider = new AuraVertexConsumerProvider(vertexConsumers, auraColour[0], auraColour[1], auraColour[2], aura);

        matrices.push();
        matrices.scale(scale, scale, scale);

        for (FeatureRenderer<T, M> renderer : this.otherFeatureRenderers) {
            renderer.render(
                    matrices,
                    auraConsumerProvider,
                    light,
                    entity,
                    limbAngle,
                    limbDistance,
                    tickDelta,
                    animationProgress,
                    headYaw,
                    headPitch
            );
        }

        matrices.translate(0.0, -((entity.getHeight() * scale) / 2f - entity.getHeight() / 2f), 0f);

        this.getContextModel().render(
                matrices,
                auraConsumerProvider.getBuffer(),
                light,
                OverlayTexture.DEFAULT_UV,
                1f,
                1f,
                1f,
                1f
        );

        matrices.pop();
    }

}

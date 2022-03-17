package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class AuraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public AuraFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        float aura = getAuraFor(entity);

        if (aura > 0) {
            this.renderAura(matrices, vertexConsumers, entity, light, aura, getAuraColourFor(entity));
        }
    }

    private void renderAura(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, int light, float aura, float[] auraColour) {
        float scale = 1f;

        matrices.push();
        matrices.scale(scale, scale, scale);
        matrices.translate(0.0, -((entity.getHeight() * scale) / 2f - entity.getHeight() / 2f), 0f);

        this.getContextModel().render(
                matrices,
                vertexConsumers.getBuffer(AuraEffectManager.getRenderLayer()),
                light,
                OverlayTexture.DEFAULT_UV,
                auraColour[0],
                auraColour[1],
                auraColour[2],
                aura
        );

        matrices.pop();
    }

    private static float getAuraFor(Entity entity) {
        return ArcanusComponents.AURA_COMPONENT.isProvidedBy(entity) ? ArcanusComponents.AURA_COMPONENT.get(entity).getAura() / (float) ArcanusComponents.AURA_COMPONENT.get(entity).getMaxAura() : 0;
    }

    private static float[] getAuraColourFor(Entity entity) {
        return ArcanusComponents.SPELL_COMPONENT.isProvidedBy(entity) ? ArcanusComponents.SPELL_COMPONENT.get(entity).getSelectedSpell().getSpellType().getRgb() : new float[]{0, 0, 0};
    }
}

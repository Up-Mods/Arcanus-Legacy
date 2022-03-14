package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.client.AuraEffectManager;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private <E extends Entity> void renderAuraForEntity(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, EntityRenderer<E> entityRenderer, Vec3d vec3d) {
        float aura = this.getAuraFor(entity);
        if (aura > 0) {
            this.renderAura(matrices, vertexConsumers, entity, entityRenderer, tickDelta, light, aura, this.getAuraColourFor(entity));
        }
    }

    @Unique
    private float getAuraFor(Entity entity) {
        return ArcanusComponents.AURA_COMPONENT.isProvidedBy(entity) ? ArcanusComponents.AURA_COMPONENT.get(entity).getAura() / (float) ArcanusComponents.AURA_COMPONENT.get(entity).getMaxAura() : 0;
    }

    @Unique
    private float[] getAuraColourFor(Entity entity) {
        return ArcanusComponents.SPELL_COMPONENT.isProvidedBy(entity) ? ArcanusComponents.SPELL_COMPONENT.get(entity).getSelectedSpell().getSpellType().getRgb() : new float[] { 0, 0, 0 };
    }

    @Unique
    private <E extends Entity> void renderAura(MatrixStack matrices, VertexConsumerProvider vertexConsumers, E entity, EntityRenderer<E> entityRenderer, float tickDelta, int light, float aura, float[] auraColour) {
        float scale = 0.5F + aura;

        matrices.push();

        matrices.scale(scale, scale, scale);

        AuraEffectManager.INSTANCE.setAuraColour(auraColour[0], auraColour[1], auraColour[2]);
        entityRenderer.render(entity, 0, tickDelta, matrices, layer -> vertexConsumers.getBuffer(AuraEffectManager.INSTANCE.getRenderLayer(layer)), light);

        matrices.pop();
    }
}

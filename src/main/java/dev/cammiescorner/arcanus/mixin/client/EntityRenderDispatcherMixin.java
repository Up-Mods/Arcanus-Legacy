package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.client.AuraShaderManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static dev.cammiescorner.arcanus.Arcanus.id;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow public Camera camera;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private <E extends Entity> void renderAuraForEntity(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, EntityRenderer<E> entityRenderer, Vec3d vec3d) {
        this.renderAura(matrices, vertexConsumers, entity, entityRenderer, tickDelta, light);
    }

    //FIXME: behaves a little oddly with vertical angles
    @Unique
    private <E extends Entity> void renderAura(MatrixStack matrices, VertexConsumerProvider vertexConsumers, E entity, EntityRenderer<E> entityRenderer, float tickDelta, int light) {
        float scale = 1.5F;

        matrices.push();

        matrices.translate(0.0, -entity.getHeight()*0.25, 0.0);
        matrices.scale(scale, scale, scale);

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-this.camera.getYaw()));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(this.camera.getPitch()));

        matrices.scale(1f, 1f, 0f);

        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-this.camera.getPitch()));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(this.camera.getYaw()));

        Vector4f transformedEntityPos = new Vector4f(new Vec3f(entity.getPos().withAxis(Direction.Axis.Y, entity.getBodyY(0.5))));

        //transformedEntityPos.transform(matrices.peek().getPositionMatrix());

        AuraShaderManager.setEntityValues(transformedEntityPos.getX(), transformedEntityPos.getY(), transformedEntityPos.getZ(), entity.getWidth(), entity.getHeight());

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(AuraShaderManager.AURA_SHADER.getRenderLayer(RENDER_LAYER));

        entityRenderer.render(entity, 0, tickDelta, matrices, layer -> vertexConsumer, light);

        matrices.pop();
    }

    private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityTranslucent(id("textures/aura.png"));
}

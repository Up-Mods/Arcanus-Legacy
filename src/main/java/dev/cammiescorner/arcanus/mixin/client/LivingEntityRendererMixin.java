package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.client.AuraEffectManager;
import dev.cammiescorner.arcanus.client.AuraVertexConsumerProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) { super(ctx); }

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V"
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void arcanus$auraShaderForFeatures(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info, float h, float j, float k, float m, float l, float n, float o, MinecraftClient minecraftClient, boolean bl, boolean bl2, boolean bl3, RenderLayer renderLayer, Iterator<FeatureRenderer<T, M>> var19, FeatureRenderer<T, M> featureRenderer) {
		float aura = AuraEffectManager.getAuraFor(livingEntity);

		if(aura > 0 && !(featureRenderer instanceof StuckArrowsFeatureRenderer)) {
			int[] auraColour = AuraEffectManager.getAuraColourFor(livingEntity);
			featureRenderer.render(matrixStack, new AuraVertexConsumerProvider(vertexConsumerProvider, auraColour[0], auraColour[1], auraColour[2], (int) (aura * 255)), i, livingEntity, o, n, g, l, k, m);
		}
	}
}

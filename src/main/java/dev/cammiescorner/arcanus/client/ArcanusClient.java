package dev.cammiescorner.arcanus.client;

import com.williambl.early_features.api.LivingEntityEarlyFeatureRendererRegistrationCallback;
import dev.cammiescorner.arcanus.common.EventHandler;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;

public class ArcanusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventHandler.clientEvents();
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		LivingEntityEarlyFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, context) -> {
			if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
				entityRenderer.addEarlyFeature(new AuraFeatureRenderer<>(playerRenderer, new HeldItemFeatureRenderer<>(playerRenderer)));
			}
		});
	}
}

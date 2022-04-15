package dev.cammiescorner.arcanus.client;

import com.williambl.early_features.api.LivingEntityEarlyFeatureRendererRegistrationCallback;
import dev.cammiescorner.arcanus.client.renderer.blocks.AmethystAltarBlockEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entities.AuraFeatureRenderer;
import dev.cammiescorner.arcanus.common.EventHandler;
import dev.cammiescorner.arcanus.common.registry.ArcanusBlockEntities;
import dev.cammiescorner.arcanus.common.registry.ArcanusKeyBinds;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;

public class ArcanusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(ArcanusBlockEntities.AMETHYST_ALTAR, AmethystAltarBlockEntityRenderer::new);
		ArcanusKeyBinds.register();
		EventHandler.clientEvents();

		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);

		LivingEntityEarlyFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, context) -> {
			if(entityRenderer instanceof PlayerEntityRenderer playerRenderer)
				entityRenderer.addEarlyFeature(new AuraFeatureRenderer<>(playerRenderer, new HeldItemFeatureRenderer<>(playerRenderer)));
		});
	}
}

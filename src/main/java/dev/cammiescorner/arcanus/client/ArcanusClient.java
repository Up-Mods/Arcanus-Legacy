package dev.cammiescorner.arcanus.client;

import com.williambl.early_features.api.LivingEntityEarlyFeatureRendererRegistrationCallback;
import dev.cammiescorner.arcanus.common.EventHandler;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventHandler.clientEvents();
		ClientTickEvents.END_CLIENT_TICK.register(AuraEffectManager.INSTANCE);
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		LivingEntityEarlyFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, context) -> {
			if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
				entityRenderer.addEarlyFeature(new AuraFeatureRenderer<>(playerRenderer));
			}
		});
	}
}

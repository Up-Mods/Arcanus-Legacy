package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.common.EventHandler;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ArcanusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventHandler.clientEvents();
		ClientTickEvents.END_CLIENT_TICK.register(AuraEffectManager.INSTANCE);
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
	}
}

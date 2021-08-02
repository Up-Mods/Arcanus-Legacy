package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.client.entity.renderer.SolarStrikeEntityRenderer;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class ArcanusClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(ModEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
		EventHandler.clientEvents();
	}
}

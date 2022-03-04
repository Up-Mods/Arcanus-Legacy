package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.common.EventHandler;
import net.fabricmc.api.ClientModInitializer;

public class ArcanusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EventHandler.clientEvents();
	}
}

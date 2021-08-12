package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.client.entity.renderer.ArcaneWallEntityRenderer;
import dev.cammiescorner.arcanus.client.entity.renderer.MagicMissileEntityRenderer;
import dev.cammiescorner.arcanus.client.entity.renderer.SolarStrikeEntityRenderer;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModKeybinds;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.*;
import org.jetbrains.annotations.Nullable;

public class ArcanusClient implements ClientModInitializer {
	@Nullable
	public static Shader renderTypeMagicShader;
	public static final RenderLayer MAGIC = RenderLayer.of("solar_strike", VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
					.shader(new RenderPhase.Shader(() -> renderTypeMagicShader))
					.writeMaskState(RenderLayer.ALL_MASK)
					.transparency(RenderLayer.LIGHTNING_TRANSPARENCY)
					.target(RenderLayer.WEATHER_TARGET)
					.build(false));

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(ModEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ModEntities.ARCANE_WALL, ArcaneWallEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ModEntities.MAGIC_MISSILE, MagicMissileEntityRenderer::new);

		ModKeybinds.register();

		EventHandler.clientEvents();
	}
}

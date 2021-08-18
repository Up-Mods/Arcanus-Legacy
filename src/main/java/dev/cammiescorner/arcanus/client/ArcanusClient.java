package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.renderer.blockentity.DisplayCaseBlockEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.ArcaneWallEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.MagicMissileEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.SolarStrikeEntityRenderer;
import dev.cammiescorner.arcanus.client.particle.DiscombobulateParticle;
import dev.cammiescorner.arcanus.client.particle.HealParticle;
import dev.cammiescorner.arcanus.client.particle.MagicMissileParticle;
import dev.cammiescorner.arcanus.client.particle.TelekineticShockParticle;
import dev.cammiescorner.arcanus.client.screens.BookshelfScreen;
import dev.cammiescorner.arcanus.core.registry.*;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
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
		ScreenRegistry.register(Arcanus.BOOKSHELF_SCREEN_HANDLER, BookshelfScreen::new);

		EntityRendererRegistry.INSTANCE.register(ModEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ModEntities.ARCANE_WALL, ArcaneWallEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(ModEntities.MAGIC_MISSILE, MagicMissileEntityRenderer::new);

		ParticleFactoryRegistry.getInstance().register(ModParticles.MAGIC_MISSILE, MagicMissileParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.TELEKINETIC_SHOCK, TelekineticShockParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.HEAL, HealParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DISCOMBOBULATE, DiscombobulateParticle.Factory::new);

		ModKeybinds.register();

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DISPLAY_CASE);

		BlockEntityRendererRegistry.INSTANCE.register(ModBlockEntities.DISPLAY_CASE, DisplayCaseBlockEntityRenderer::new);

		EventHandler.clientEvents();
	}
}

package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.particle.DiscombobulateParticle;
import dev.cammiescorner.arcanus.client.particle.HealParticle;
import dev.cammiescorner.arcanus.client.particle.MagicMissileParticle;
import dev.cammiescorner.arcanus.client.particle.TelekineticShockParticle;
import dev.cammiescorner.arcanus.client.renderer.blockentity.DisplayCaseBlockEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.ArcaneBarrierEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.MagicMissileEntityRenderer;
import dev.cammiescorner.arcanus.client.renderer.entity.SolarStrikeEntityRenderer;
import dev.cammiescorner.arcanus.client.screens.BookshelfScreen;
import dev.cammiescorner.arcanus.core.registry.ModBlockEntities;
import dev.cammiescorner.arcanus.core.registry.ModBlocks;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModParticles;
import dev.cammiescorner.arcanus.core.util.EventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ArcanusClient implements ClientModInitializer {
	@Nullable
	public static Shader renderTypeMagicShader;
	public static final RenderLayer MAGIC = RenderLayer.of("magic", VertexFormats.POSITION_COLOR,
			VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
					.shader(new RenderPhase.Shader(() -> renderTypeMagicShader))
					.writeMaskState(RenderLayer.ALL_MASK)
					.transparency(RenderLayer.LIGHTNING_TRANSPARENCY)
					.target(RenderLayer.WEATHER_TARGET)
					.build(false));

	@Override
	public void onInitializeClient() {
		screenRegistry();
		entityRendererRegistry();
		particleFactoryRegistry();
		// ModKeybinds.register();
		itemPredicateRegistry();
		blockRenderLayerRegistry();
		blockEntityRendererRegistry();
		EventHandler.clientEvents();
	}

	public void screenRegistry() {
		ScreenRegistry.register(Arcanus.BOOKSHELF_SCREEN_HANDLER, BookshelfScreen::new);
	}

	public void entityRendererRegistry() {
		EntityRendererRegistry.register(ModEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.ARCANE_BARRIER, ArcaneBarrierEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_MISSILE, MagicMissileEntityRenderer::new);
	}

	public void particleFactoryRegistry() {
		ParticleFactoryRegistry.getInstance().register(ModParticles.MAGIC_MISSILE, MagicMissileParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.TELEKINETIC_SHOCK, TelekineticShockParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.HEAL, HealParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DISCOMBOBULATE, DiscombobulateParticle.Factory::new);
	}

	public void itemPredicateRegistry() {
		FabricModelPredicateProviderRegistry.register(new Identifier(Arcanus.MOD_ID, "mana"), (stack, world, entity, seed) -> {
			NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

			if(tag == null)
				return 0;

			return tag.getInt("Mana") / 4F;
		});
	}

	public void blockRenderLayerRegistry() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DISPLAY_CASE);
	}

	public void blockEntityRendererRegistry() {
		BlockEntityRendererRegistry.register(ModBlockEntities.DISPLAY_CASE, DisplayCaseBlockEntityRenderer::new);
	}
}

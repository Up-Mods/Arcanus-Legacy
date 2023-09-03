package dev.cammiescorner.arcanus.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
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
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ArcanusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		screenRegistry();
		entityRendererRegistry();
		particleFactoryRegistry();
		itemPredicateRegistry();
		blockRenderLayerRegistry();
		blockEntityRendererRegistry();
		EventHandler.clientEvents();
	}

	public static RenderLayer getMagicCircles(Identifier texture) {
		return RenderLayer.of(
				Arcanus.id("magic").toString(),
				VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
				VertexFormat.DrawMode.QUADS,
				256,
				false,
				true,
				RenderLayer.MultiPhaseParameters.builder()
						.shader(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
						.texture(new RenderPhase.Texture(texture, false, false))
						.overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
						.transparency(RenderLayer.ADDITIVE_TRANSPARENCY)
						.writeMaskState(RenderLayer.ALL_MASK)
						.cull(RenderPhase.DISABLE_CULLING)
						.build(false)
		);
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

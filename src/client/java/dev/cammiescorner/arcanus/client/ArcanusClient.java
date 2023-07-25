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
import dev.cammiescorner.arcanus.client.util.ClientEventHandler;
import dev.cammiescorner.arcanus.core.registry.ModBlockEntities;
import dev.cammiescorner.arcanus.core.registry.ModBlocks;
import dev.cammiescorner.arcanus.core.registry.ModEntities;
import dev.cammiescorner.arcanus.core.registry.ModParticles;
import dev.cammiescorner.arcanus.core.util.Pattern;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ArcanusClient implements ClientModInitializer {
	public static final List<Pattern> pattern = new ArrayList<>(3);
	public static boolean unfinishedSpell = true;
	public static int timer = 0;

	@Override
	public void onInitializeClient() {
		ClientEventHandler.clientEvents();
		ParticleFactoryRegistry.getInstance().register(ModParticles.MAGIC_MISSILE, MagicMissileParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.TELEKINETIC_SHOCK, TelekineticShockParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.HEAL, HealParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DISCOMBOBULATE, DiscombobulateParticle.Factory::new);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DISPLAY_CASE);
		BlockEntityRendererRegistry.register(ModBlockEntities.DISPLAY_CASE, DisplayCaseBlockEntityRenderer::new);

		EntityRendererRegistry.register(ModEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.ARCANE_BARRIER, ArcaneBarrierEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_MISSILE, MagicMissileEntityRenderer::new);

		HandledScreens.register(Arcanus.BOOKSHELF_SCREEN_HANDLER, BookshelfScreen::new);

		ModelPredicateProviderRegistry.register(new Identifier(Arcanus.MOD_ID, "mana"), (stack, world, entity, seed) -> {
			NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

			if (tag == null)
				return 0;

			return tag.getInt("Mana") / 4F;
		});
	}
}

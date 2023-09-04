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
import dev.cammiescorner.arcanus.registry.*;
import dev.cammiescorner.arcanus.util.EventHandler;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class ArcanusClient implements ClientModInitializer {
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

    @Override
    public void onInitializeClient(ModContainer mod) {
        HandledScreens.register(ArcanusScreens.BOOKSHELF_SCREEN_HANDLER, BookshelfScreen::new);

        EntityRendererRegistry.register(ArcanusEntities.SOLAR_STRIKE, SolarStrikeEntityRenderer::new);
        EntityRendererRegistry.register(ArcanusEntities.ARCANE_BARRIER, ArcaneBarrierEntityRenderer::new);
        EntityRendererRegistry.register(ArcanusEntities.MAGIC_MISSILE, MagicMissileEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.MAGIC_MISSILE, MagicMissileParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.TELEKINETIC_SHOCK, TelekineticShockParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.HEAL, HealParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.DISCOMBOBULATE, DiscombobulateParticle.Factory::new);

        BlockEntityRendererFactories.register(ArcanusBlockEntities.DISPLAY_CASE, DisplayCaseBlockEntityRenderer::new);
        BlockRenderLayerMap.put(RenderLayer.getCutout(), ArcanusBlocks.DISPLAY_CASE);

        EventHandler.clientEvents();

        ModelPredicateProviderRegistry.register(Arcanus.id("mana"), (stack, world, entity, seed) -> {
            NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

            if (tag == null)
                return 0;

            return tag.getInt("Mana") / 4F;
        });
    }

}

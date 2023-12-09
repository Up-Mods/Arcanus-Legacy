package dev.cammiescorner.arcanus.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class ArcanusClient implements ClientModInitializer {

    public static int manaTimer;

    public static boolean shouldRenderManaBar() {
        return manaTimer > 0;
    }

    public static RenderType getMagicCircles(ResourceLocation texture) {
        return RenderType.create(
                Arcanus.id("magic").toString(),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY)
                        .setWriteMaskState(RenderType.COLOR_DEPTH_WRITE)
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false)
        );
    }

    @Override
    public void onInitializeClient(ModContainer mod) {
        MenuScreens.register(ArcanusScreens.BOOKSHELF_SCREEN_HANDLER.get(), BookshelfScreen::new);

        EntityRendererRegistry.register(ArcanusEntities.SOLAR_STRIKE.get(), SolarStrikeEntityRenderer::new);
        EntityRendererRegistry.register(ArcanusEntities.ARCANE_BARRIER.get(), ArcaneBarrierEntityRenderer::new);
        EntityRendererRegistry.register(ArcanusEntities.MAGIC_MISSILE.get(), MagicMissileEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.MAGIC_MISSILE.get(), MagicMissileParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.TELEKINETIC_SHOCK.get(), TelekineticShockParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.HEAL.get(), HealParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ArcanusParticles.DISCOMBOBULATE.get(), DiscombobulateParticle.Factory::new);

        BlockEntityRenderers.register(ArcanusBlockEntities.DISPLAY_CASE.get(), DisplayCaseBlockEntityRenderer::new);
        BlockRenderLayerMap.put(RenderType.cutout(), ArcanusBlocks.DISPLAY_CASE.get());

        EventHandler.clientEvents();

        ItemProperties.registerGeneric(Arcanus.id("mana"), (stack, world, entity, seed) -> {
            CompoundTag tag = stack.getTagElement(Arcanus.MODID);

            if (tag == null)
                return 0;

            return tag.getInt("Mana") / 4F;
        });
    }

}

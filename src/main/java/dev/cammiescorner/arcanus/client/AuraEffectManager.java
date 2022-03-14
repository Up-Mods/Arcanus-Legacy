package dev.cammiescorner.arcanus.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.mixin.client.FramebufferAccessor;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedFramebuffer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform4f;
import ladysnake.satin.api.util.RenderLayerHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;

import static dev.cammiescorner.arcanus.Arcanus.id;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

/**
 * Manages the Aura rendering effect. Auras are rendered to a separate framebuffer with custom shaders and then
 * composited into the main framebuffer. This uses similar techniques to <a href="https://github.com/Ladysnake/Requiem/blob/ad68fa534c6b6d4c64be86162eb35e67140138c6/src/main/java/ladysnake/requiem/client/ShadowPlayerFx.java">Requiem's Shadow Player rendering.</a>
 */
public final class AuraEffectManager implements ClientTickEvents.EndTick, EntitiesPreRenderCallback, ShaderEffectRenderCallback {
    public static final AuraEffectManager INSTANCE = new AuraEffectManager();

    private final MinecraftClient client = MinecraftClient.getInstance();

    private final ManagedCoreShader auraCoreShader = ShaderEffectManager.getInstance().manageCoreShader(id("aura_core"));
    private final Uniform4f colourUniform = auraCoreShader.findUniform4f("ColorModulator");

    private final ManagedShaderEffect auraPostShader = ShaderEffectManager.getInstance().manage(id("shaders/post/aura.json"), this::assignDepthTexture);
    private final ManagedFramebuffer auraFramebuffer = auraPostShader.getTarget("auras");
    private final Uniform1f timeUniform = auraPostShader.findUniform1f("STime");

    private int ticks;
    private boolean renderedAuras;

    @Override
    public void onEndTick(MinecraftClient client) {
        ticks++;
    }

    @Override
    public void beforeEntitiesRender(@NotNull Camera camera, @NotNull Frustum frustum, float tickDelta) {
        timeUniform.set((ticks + tickDelta) * 0.05f);
        if (!this.auraPostShader.isInitialized()) {
            try {
                this.auraPostShader.initialize();
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to initialize aura shader", e);
            }
        }
        this.renderedAuras = false;
    }

    @Override
    public void renderShaderEffects(float tickDelta) {
        if (this.renderedAuras) {
            auraPostShader.render(tickDelta);
        }
    }

    public void setAuraColour(float r, float g, float b) {
        this.colourUniform.set(r, g, b, 1f);
    }

    public void beginAuraFramebufferWrite() {
        Framebuffer auraFramebuffer = this.auraFramebuffer.getFramebuffer();
        if (auraFramebuffer != null) {
            auraFramebuffer.beginWrite(false);
            RenderSystem.depthMask(false);
            if (!this.renderedAuras) {
                // clearing color but not depth
                float[] clearColor = ((FramebufferAccessor) auraFramebuffer).getClearColor();

                RenderSystem.clearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
                RenderSystem.clear(GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

                this.renderedAuras = true;
            }
        }
    }

    private void endAuraFramebufferWrite() {
        this.client.getFramebuffer().beginWrite(false);
        RenderSystem.depthMask(true);
    }

    private void assignDepthTexture(ManagedShaderEffect managedShaderEffect) {
        client.getFramebuffer().beginWrite(false);
        int depthTexturePtr = client.getFramebuffer().getDepthAttachment();
        if (depthTexturePtr > -1) {
            auraFramebuffer.beginWrite(false);
            // Use the same depth texture for our framebuffer as the main one
            GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexturePtr, 0);
        }
    }

    public RenderLayer getRenderLayer(RenderLayer base) {
        return AuraRenderLayers.getAuraRenderLayer(auraCoreShader.getRenderLayer(base));
    }

    /**
     * Helper for creating copies of {@link RenderLayer RenderLayers} which render to the aura framebuffer.
     */
    private static final class AuraRenderLayers extends RenderLayer {
        // have to extend RenderLayer to access RenderPhase.Target

        private static final Target AURA_TARGET = new Target(
                "requiem:aura_target",
                AuraEffectManager.INSTANCE::beginAuraFramebufferWrite,
                AuraEffectManager.INSTANCE::endAuraFramebufferWrite
        );
        // no need to create instances of this

        private AuraRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
        }

        /**
         * Copies a {@link RenderLayer} and makes the copy render to the Aura framebuffer.
         *
         * @param original  the original RenderLayer
         * @return      the copied render layer
         *
         * @see ladysnake.satin.api.managed.ManagedCoreShader#getRenderLayer(RenderLayer)
         */
        public static RenderLayer getAuraRenderLayer(RenderLayer original) {
            return RenderLayerHelper.copy(original, "arcanus:aura", builder -> builder.target(AURA_TARGET));
        }
    }
}

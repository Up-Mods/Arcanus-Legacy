package dev.cammiescorner.arcanus.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedFramebuffer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static dev.cammiescorner.arcanus.Arcanus.id;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

/**
 * Manages the Aura rendering effect. Auras are rendered to a separate framebuffer with a custom renderlayer and then
 * composited into the main framebuffer with a post shader. This uses similar techniques to
 * <a href="https://github.com/Ladysnake/Requiem/blob/ad68fa534c6b6d4c64be86162eb35e67140138c6/src/main/java/ladysnake/requiem/client/ShadowPlayerFx.java">Requiem's Shadow Player rendering.</a>
 */
public final class AuraEffectManager implements EntitiesPreRenderCallback, ShaderEffectRenderCallback {
	public static final AuraEffectManager INSTANCE = new AuraEffectManager();
	private final MinecraftClient client = MinecraftClient.getInstance();
	private final ManagedShaderEffect auraPostShader = ShaderEffectManager.getInstance().manage(id("shaders/post/aura.json"), this::assignDepthTexture);
	private final ManagedFramebuffer auraFramebuffer = auraPostShader.getTarget("auras");
	private boolean auraBufferCleared;

	public static float getAuraFor(Entity entity) {
		return ArcanusComponents.AURA_COMPONENT.isProvidedBy(entity) && ArcanusComponents.AURA_FADE_COMPONENT.isProvidedBy(entity) && ArcanusHelper.getAuraFade(entity) > 0 ?
				(ArcanusComponents.AURA_COMPONENT.get(entity).getAura() / (float) ArcanusComponents.AURA_COMPONENT.get(entity).getMaxAura()) * ArcanusHelper.getAuraFade(entity) : 0;
	}

	public static int[] getAuraColourFor(Entity entity) {
		return ArcanusComponents.CURRENT_SPELL_COMPONENT.isProvidedBy(entity) ? ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity).getSelectedSpell().getSpellType().getRgbInt() : new int[]{0, 0, 0};
	}

	@Override
	public void beforeEntitiesRender(@NotNull Camera camera, @NotNull Frustum frustum, float tickDelta) {
		this.auraBufferCleared = false;
	}

	@Override
	public void renderShaderEffects(float tickDelta) {
		if(this.auraBufferCleared) {
			auraPostShader.render(tickDelta);
		}
	}

	/**
	 * Binds aura framebuffer for use and clears it if necessary.
	 */
	public void beginAuraFramebufferUse() {
		Framebuffer auraFramebuffer = this.auraFramebuffer.getFramebuffer();

		if(auraFramebuffer != null) {
			auraFramebuffer.beginWrite(false);

			if(!this.auraBufferCleared) {
				// clear framebuffer colour (but not depth)
				float[] clearColor = auraFramebuffer.clearColor;
				RenderSystem.clearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3]);
				RenderSystem.clear(GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);

				this.auraBufferCleared = true;
			}
		}
	}

	/**
	 * Unbinds aura framebuffer for use and undoes changes made in {@link #beginAuraFramebufferUse()}.
	 */
	private void endAuraFramebufferUse() {
		this.client.getFramebuffer().beginWrite(false);
	}

	/**
	 * Makes the aura framebuffer use the same depth texture as the main framebuffer. Run when the aura post shader
	 * is initialised.
	 *
	 * @param managedShaderEffect shader effect being initialised
	 */
	private void assignDepthTexture(ManagedShaderEffect managedShaderEffect) {
		client.getFramebuffer().beginWrite(false);
		int depthTexturePtr = client.getFramebuffer().getDepthAttachment();
		if(depthTexturePtr > -1) {
			auraFramebuffer.beginWrite(false);
			GlStateManager._glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexturePtr, 0);
		}
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with a given texture
	 *
	 * @param texture the identifier of the texture to use
	 *
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer(Identifier texture) {
		return texture == null ? getRenderLayer() : AuraRenderLayers.AURA_LAYER.apply(texture);
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with the same texture as a given render layer
	 *
	 * @param base the render layer to take the texture from
	 *
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer(@NotNull RenderLayer base) {
		return AuraRenderLayers.getRenderLayerWithTextureFrom(base);
	}

	/**
	 * Gets the {@link RenderLayer} for rendering auras with a default texture
	 *
	 * @return the render layer
	 */
	public static RenderLayer getRenderLayer() {
		return AuraRenderLayers.DEFAULT_AURA_LAYER;
	}

	/**
	 * Helper for the creating and holding the aura render layers and target
	 */
	private static final class AuraRenderLayers extends RenderLayer {
		// have to extend RenderLayer to access a few of these things

		private static final Target AURA_TARGET = new Target("arcanus:aura_target", AuraEffectManager.INSTANCE::beginAuraFramebufferUse, AuraEffectManager.INSTANCE::endAuraFramebufferUse);

		private static final Identifier WHITE_TEXTURE = new Identifier("misc/white.png");

		private static final Function<Identifier, RenderLayer> AURA_LAYER = Util.memoize(id -> RenderLayer.of("aura", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(OUTLINE_SHADER).writeMaskState(COLOR_MASK).transparency(TRANSLUCENT_TRANSPARENCY).target(AURA_TARGET).texture(new Texture(id, false, false)).build(false)));

		private static final RenderLayer DEFAULT_AURA_LAYER = AURA_LAYER.apply(WHITE_TEXTURE);

		// no need to create instances of this
		private AuraRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}

		/**
		 * Extracts the texture from a render layer and creates an aura render layer with it.
		 *
		 * @param base the render layer to take the texture from
		 *
		 * @return the aura render layer
		 */
		private static RenderLayer getRenderLayerWithTextureFrom(RenderLayer base) {
			if (base instanceof RenderLayer.MultiPhase multiPhase) {
				return AURA_LAYER.apply(multiPhase.getPhases().texture.getId().orElse(WHITE_TEXTURE));
			} else {
				return DEFAULT_AURA_LAYER;
			}
		}
	}
}

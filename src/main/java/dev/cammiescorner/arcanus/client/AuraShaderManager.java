package dev.cammiescorner.arcanus.client;

import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform2f;
import ladysnake.satin.api.managed.uniform.Uniform3f;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import org.jetbrains.annotations.NotNull;

import static dev.cammiescorner.arcanus.Arcanus.id;

public final class AuraShaderManager implements ClientTickEvents.EndTick, EntitiesPreRenderCallback {
    public static final AuraShaderManager INSTANCE = new AuraShaderManager();
    public static final ManagedCoreShader AURA_SHADER = ShaderEffectManager.getInstance().manageCoreShader(id("aura"));
    private static final Uniform1f TIME_UNIFORM = AURA_SHADER.findUniform1f("STime");
    private static final Uniform3f ENTITY_CENTRE = AURA_SHADER.findUniform3f("EntityCentre");
    private static final Uniform2f ENTITY_SIZE = AURA_SHADER.findUniform2f("EntitySize");

    private static int ticks;

    public static void setEntityValues(float x, float y, float z, float width, float height) {
        ENTITY_CENTRE.set(x, y, z);
        ENTITY_SIZE.set(width, height);
    }

    @Override
    public void beforeEntitiesRender(@NotNull Camera camera, @NotNull Frustum frustum, float tickDelta) {
        TIME_UNIFORM.set((ticks + tickDelta) * 0.05f);
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        ticks++;
    }
}

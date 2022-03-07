package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.common.EventHandler;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedFramebuffer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.util.RenderLayerHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static dev.cammiescorner.arcanus.Arcanus.id;

public class ArcanusClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EventHandler.clientEvents();
		ClientTickEvents.END_CLIENT_TICK.register(AuraShaderManager.INSTANCE);
		EntitiesPreRenderCallback.EVENT.register(AuraShaderManager.INSTANCE);
	}
}

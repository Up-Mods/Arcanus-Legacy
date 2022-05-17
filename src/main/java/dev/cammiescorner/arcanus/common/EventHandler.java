package dev.cammiescorner.arcanus.common;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.events.client.KeyBindingCallback;
import dev.cammiescorner.arcanus.common.components.world.AltarStructureComponent;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusKeyBinds;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldLoadEvents;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

public class EventHandler {
	public static final Identifier HUD_ELEMENTS = Arcanus.id("textures/gui/hud_elements.png");

	@Environment(EnvType.CLIENT)
	public static void clientEvents() {
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				PlayerEntity player = client.player;

				if(ArcanusHelper.isCasting(player) && ArcanusKeyBinds.spellInvKey.isPressed()) {
					client.mouse.unlockCursor();
				}
			}
		});

		ClientTickEvents.START.register(client -> {
			if(client.player != null) {
				SetCastingPacket.send(ArcanusKeyBinds.castingMode.isPressed());
			}
		});

		KeyBindingCallback.UNPRESSED.register((key, modifiers) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				PlayerEntity player = client.player;

				if(ArcanusHelper.isCasting(player) && ArcanusKeyBinds.spellInvKey.matchesKey(key.getKeyCode(), key.getKeyCode()))
					client.mouse.lockCursor();
			}
		});
	}

	public static void commonEvents() {
		CommandRegistrationCallback.EVENT.register(ArcanusCommands::init);

		ServerWorldLoadEvents.LOAD.register((server, world) -> ArcanusHelper.constructStructureMap(world));
		ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
			if(server != null)
				server.getWorlds().forEach(ArcanusHelper::constructStructureMap);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			World world = handler.player.world;
			ComponentKey<AltarStructureComponent> key = ArcanusComponents.ALTAR_STRUCTURE_COMPONENT;
			key.sync(world, key.get(world), player -> player.getUuid().equals(handler.player.getUuid()));
		});
	}
}

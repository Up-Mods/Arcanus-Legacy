package dev.cammiescorner.arcanus.common;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusKeyBinds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class EventHandler {
	private static final Identifier HUD_ELEMENTS = Arcanus.id("textures/gui/hud_elements.png");

	@Environment(EnvType.CLIENT)
	public static void clientEvents() {
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();

			if(client.player != null) {
				PlayerEntity player = client.player;

				if(ArcanusHelper.isCasting(player) && ArcanusKeyBinds.spellInvKey.isPressed()) {

				}
			}
		});

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if(client.player != null) {
				PlayerEntity player = client.player;

				SetCastingPacket.send(ArcanusKeyBinds.castingMode.isPressed());

				if(ArcanusHelper.isCasting(player) && ArcanusHelper.keyUnpressed(ArcanusKeyBinds.spellInvKey))
					System.out.println("Beeeeeep");
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> ArcanusHelper.wasQPressed = ArcanusKeyBinds.spellInvKey.isPressed());
	}

	public static void commonEvents() {
		//-----Command Callback-----//
		CommandRegistrationCallback.EVENT.register(ArcanusCommands::init);
	}
}

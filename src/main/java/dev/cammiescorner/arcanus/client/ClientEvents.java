package dev.cammiescorner.arcanus.client;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.events.client.KeyBindingCallback;
import dev.cammiescorner.arcanus.common.packets.c2s.SetCastingPacket;
import dev.cammiescorner.arcanus.common.registry.ArcanusKeyBinds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class ClientEvents {
	public static final Identifier HUD_ELEMENTS = Arcanus.id("textures/gui/hud_elements.png");

	@Environment(EnvType.CLIENT)
	public static void events() {
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
}

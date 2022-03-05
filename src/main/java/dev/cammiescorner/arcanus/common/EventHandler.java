package dev.cammiescorner.arcanus.common;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.components.entity.AuraComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusCommands;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;

public class EventHandler {
	private static final Identifier HUD_ELEMENTS = Arcanus.id("textures/gui/hud_elements.png");

	@Environment(EnvType.CLIENT)
	public static void clientEvents() {
		final MinecraftClient client = MinecraftClient.getInstance();
		var auraTimer = new Object() {
			int value;
		};

		//-----HUD Render Callback-----//
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			if(client.cameraEntity instanceof PlayerEntity player && !player.isSpectator() && !player.isCreative()) {
				AuraComponent auraComponent = ArcanusComponents.AURA_COMPONENT.get(player);
				int aura = auraComponent.getAura();

				if(player.getMainHandStack().getItem() instanceof SwordItem || aura < auraComponent.getMaxAura())
					auraTimer.value = Math.min(auraTimer.value + 1, 40);
				else
					auraTimer.value = Math.max(auraTimer.value - 1, 0);

				if(auraTimer.value > 0) {
					int scaledWidth = client.getWindow().getScaledWidth();
					int scaledHeight = client.getWindow().getScaledHeight();
					int x = scaledWidth / 2 + 82;
					int y = scaledHeight - (player.isCreative() ? 34 : 49);
					float alpha = auraTimer.value > 20 ? 1F : auraTimer.value / 20F;

					RenderSystem.enableBlend();
					RenderSystem.setShaderTexture(0, HUD_ELEMENTS);
					RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

					// Draw background
					for(int i = 0; i < 10; i++)
						DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

					// Draw full aura orb
					for(int i = 0; i < aura / 2; i++)
						DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);

					// Draw half aura orb
					if(aura % 2 == 1)
						DrawableHelper.drawTexture(matrices, x - ((aura / 2) * 8), y, 8, 0, 8, 8, 256, 256);
				}
			}
		});
	}

	public static void commonEvents() {
		//-----Command Callback-----//
		CommandRegistrationCallback.EVENT.register(ArcanusCommands::init);
	}
}

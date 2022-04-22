package dev.cammiescorner.arcanus.common.registry;

import com.mojang.blaze3d.platform.InputUtil;
import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import net.minecraft.client.option.StickyKeyBind;
import org.lwjgl.glfw.GLFW;

public class ArcanusKeyBinds {
	public static final String ARCANUS_CATEGORY = "category." + Arcanus.MOD_ID + ".keys";
	public static KeyBind castingMode;
	public static KeyBind spellInvKey;

	public static void register() {
		castingMode = KeyBindingHelper.registerKeyBinding(new StickyKeyBind(
				"key." + Arcanus.MOD_ID + ".swapCastingMode",
				GLFW.GLFW_KEY_X,
				ArcanusKeyBinds.ARCANUS_CATEGORY,
				() -> true
		));

		spellInvKey = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key." + Arcanus.MOD_ID + ".openSpellInv",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				ArcanusKeyBinds.ARCANUS_CATEGORY
		));
	}
}

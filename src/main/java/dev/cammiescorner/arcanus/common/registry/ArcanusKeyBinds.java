package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ArcanusKeyBinds {
	public static final String ARCANUS_CATEGORY = "category." + Arcanus.MOD_ID + ".keys";
	public static KeyBinding castingMode;
	public static KeyBinding spellInvKey;

	public static void register() {
		castingMode = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding(
				"key." + Arcanus.MOD_ID + ".swapCastingMode",
				GLFW.GLFW_KEY_X,
				ArcanusKeyBinds.ARCANUS_CATEGORY,
				() -> true
		));

		spellInvKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + Arcanus.MOD_ID + ".openSpellInv",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_ALT,
				ArcanusKeyBinds.ARCANUS_CATEGORY
		));
	}
}

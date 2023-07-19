package dev.cammiescorner.arcanus.client.init;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {
	//-----Keybinding Map-----//
	public static final List<KeyBinding> KEYBINDINGS = new ArrayList<>();

	//-----Keybindings-----//
	public static final KeyBinding ACTIVATE_AMULET = create("activate_amulet", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G);
	public static final KeyBinding SPELL_MODIFIER = create("spell_modifier", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT);

	//-----Registry-----//
	public static void register() {
		KEYBINDINGS.forEach(KeyBindingHelper::registerKeyBinding);
	}

	private static KeyBinding create(String name, InputUtil.Type type, int key) {
		KeyBinding keyBinding = new KeyBinding("key." + Arcanus.MOD_ID + "." + name, type, key, "category." + Arcanus.MOD_ID + ".general");
		KEYBINDINGS.add(keyBinding);
		return keyBinding;
	}
}

package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.api.events.client.KeyBindingCallback;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	@Inject(method = "onKey", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V",
			ordinal = 0
	))
	public void arcanus$onUnpressEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
		KeyBindingCallback.UNPRESSED.invoker().unpress(InputUtil.fromKeyCode(key, scancode), modifiers);
	}
}

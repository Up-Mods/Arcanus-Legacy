package dev.cammiescorner.arcanus.core.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	@Final
	private SmoothUtil cursorXSmoother;
	@Shadow
	@Final
	private SmoothUtil cursorYSmoother;
	@Shadow
	private double lastMouseUpdateTime;
	@Shadow
	private double cursorDeltaX;
	@Shadow
	private double cursorDeltaY;

	@Shadow
	public abstract boolean isCursorLocked();

	/**
	 * @author Cammie
	 * @reason Needed to invert mouse X axis too
	 */
	@Overwrite
	public void updateMouse() {
		double time = GlfwUtil.getTime();
		double deltaTime = time - this.lastMouseUpdateTime;
		this.lastMouseUpdateTime = time;

		if(this.isCursorLocked() && this.client.isWindowFocused()) {
			double mouseSensitivity = this.client.options.mouseSensitivity * 0.6000000238418579D + 0.20000000298023224D;
			double mouseSensitivityCubed = mouseSensitivity * mouseSensitivity * mouseSensitivity;
			double fuckIfIKnowDude = mouseSensitivityCubed * 8.0D;
			double mouseX;
			double mouseY;

			if(this.client.options.smoothCameraEnabled) {
				double smoothedMouseX = this.cursorXSmoother.smooth(this.cursorDeltaX * fuckIfIKnowDude, deltaTime * fuckIfIKnowDude);
				double smoothedMouseY = this.cursorYSmoother.smooth(this.cursorDeltaY * fuckIfIKnowDude, deltaTime * fuckIfIKnowDude);
				mouseX = smoothedMouseX;
				mouseY = smoothedMouseY;
			}
			else if(this.client.options.getPerspective().isFirstPerson() && this.client.player.isUsingSpyglass()) {
				this.cursorXSmoother.clear();
				this.cursorYSmoother.clear();
				mouseX = this.cursorDeltaX * mouseSensitivityCubed;
				mouseY = this.cursorDeltaY * mouseSensitivityCubed;
			}
			else {
				this.cursorXSmoother.clear();
				this.cursorYSmoother.clear();
				mouseX = this.cursorDeltaX * fuckIfIKnowDude;
				mouseY = this.cursorDeltaY * fuckIfIKnowDude;
			}

			this.cursorDeltaX = 0.0D;
			this.cursorDeltaY = 0.0D;
			int invertMouse = 1;

			if(this.client.options.invertYMouse)
				invertMouse = -1;

			this.client.getTutorialManager().onUpdateMouse(mouseX, mouseY);

			if(this.client.player != null)
				this.client.player.changeLookDirection(mouseX * invertMouse, mouseY * invertMouse);

		}
		else {
			this.cursorDeltaX = 0.0D;
			this.cursorDeltaY = 0.0D;
		}
	}
}

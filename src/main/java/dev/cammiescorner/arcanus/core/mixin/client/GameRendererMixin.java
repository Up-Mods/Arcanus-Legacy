package dev.cammiescorner.arcanus.core.mixin.client;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import net.minecraft.client.gl.Program;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(method = "loadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 53), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void loadShaders(ResourceManager manager, CallbackInfo info, List<Program> list, List<Pair<Shader, Consumer<Shader>>> list2) throws IOException {
		list2.add(Pair.of(new Shader(manager, "rendertype_arcanus_magic", VertexFormats.POSITION_COLOR), (shader) -> ArcanusClient.renderTypeMagicShader = shader));
	}
}

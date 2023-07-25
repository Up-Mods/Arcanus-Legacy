package dev.cammiescorner.arcanus.client.renderer;

import net.minecraft.client.render.*;
import org.jetbrains.annotations.Nullable;

public class ArcanusRenderLayers {
    @Nullable
    public static Shader renderTypeMagicShader;
    public static final RenderLayer MAGIC = RenderLayer.of("magic", VertexFormats.POSITION_COLOR,
            VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
                    .shader(new RenderPhase.Shader(() -> renderTypeMagicShader))
                    .writeMaskState(RenderLayer.ALL_MASK)
                    .transparency(RenderLayer.LIGHTNING_TRANSPARENCY)
                    .target(RenderLayer.WEATHER_TARGET)
                    .build(false));
}

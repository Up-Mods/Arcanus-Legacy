package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(value = FluidRenderer.class, priority = 9999)
public class FluidRendererMixin {
	@ModifyVariable(method = "render", at = @At(value = "CONSTANT",
			args = "intValue=16",
			ordinal = 0,
			shift = At.Shift.BEFORE
	), ordinal = 0)
	public int arcanus$baterWucket(int colour, BlockRenderView world, BlockPos pos) {
		Optional<PurpleWaterComponent> optional = ArcanusComponents.PURPLE_WATER_COMPONENT.maybeGet(MinecraftClient.getInstance().world.getChunk(pos)).filter(component -> component.isPosNearAltar(pos.toImmutable()));

		if(optional.isPresent())
			return 0x8408e3;
		else
			return colour;
	}
}

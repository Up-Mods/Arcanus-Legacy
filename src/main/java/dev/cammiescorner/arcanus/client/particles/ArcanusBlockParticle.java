package dev.cammiescorner.arcanus.client.particles;

import net.minecraft.block.BlockState;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class ArcanusBlockParticle extends BlockDustParticle {
	public ArcanusBlockParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockState state, BlockPos pos) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, state, pos);
		gravityStrength = 0;
	}
}

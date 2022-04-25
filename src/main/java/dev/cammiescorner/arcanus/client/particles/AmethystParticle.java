package dev.cammiescorner.arcanus.client.particles;

import net.minecraft.block.Blocks;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class AmethystParticle extends BlockDustParticle {
	public AmethystParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, BlockPos pos) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, Blocks.AMETHYST_CLUSTER.getDefaultState(), pos);
		gravityStrength = 0;
	}
}

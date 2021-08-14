package dev.cammiescorner.arcanus.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class HealParticle extends SpriteBillboardParticle {
	public HealParticle(ClientWorld clientWorld, double posX, double posY, double posZ) {
		super(clientWorld, posX, posY, posZ, 0, 0.01, 0);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			HealParticle particle = new HealParticle(clientWorld, posX, posY, posZ);
			particle.setSpriteForAge(spriteProvider);
			return particle;
		}
	}
}

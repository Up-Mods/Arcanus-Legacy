package dev.cammiescorner.arcanus.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class DiscombobulateParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public DiscombobulateParticle(ClientWorld clientWorld, double posX, double posY, double posZ, SpriteProvider spriteProvider) {
        super(clientWorld, posX, posY, posZ, 0, 0, 0);
        this.maxAge = (int) (20 * MathHelper.clamp(random.nextFloat(), 0.5, 1.0));
        this.spriteProvider = spriteProvider;
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
    }

    @Override
    public void tick() {
        setSpriteForAge(spriteProvider);
        super.tick();
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
            DiscombobulateParticle particle = new DiscombobulateParticle(clientWorld, posX, posY, posZ, spriteProvider);
            particle.setSpriteForAge(spriteProvider);
            return particle;
        }
    }
}

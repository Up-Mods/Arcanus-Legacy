package dev.cammiescorner.arcanus.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class DiscombobulateParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    public DiscombobulateParticle(ClientLevel clientWorld, double posX, double posY, double posZ, SpriteSet spriteProvider) {
        super(clientWorld, posX, posY, posZ, 0, 0, 0);
        this.lifetime = (int) (20 * Mth.clamp(random.nextFloat(), 0.5, 1.0));
        this.spriteProvider = spriteProvider;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    @Override
    public void tick() {
        setSpriteFromAge(spriteProvider);
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @ClientOnly
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientLevel clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
            DiscombobulateParticle particle = new DiscombobulateParticle(clientWorld, posX, posY, posZ, spriteProvider);
            particle.setSpriteFromAge(spriteProvider);
            return particle;
        }
    }
}

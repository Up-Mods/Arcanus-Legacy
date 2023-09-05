package dev.cammiescorner.arcanus.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class MagicMissileParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    public MagicMissileParticle(ClientLevel world, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, posX, posY, posZ, 0, 0, 0);
        this.lifetime = (int) (15 * Mth.clamp(random.nextFloat(), 0.5, 1.0));
        this.spriteProvider = spriteProvider;
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
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
            MagicMissileParticle particle = new MagicMissileParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ, spriteProvider);
            particle.setSpriteFromAge(spriteProvider);
            return particle;
        }
    }
}

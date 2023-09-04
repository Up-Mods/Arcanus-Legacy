package dev.cammiescorner.arcanus.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;

public class TelekineticShockParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    public TelekineticShockParticle(ClientWorld clientWorld, double posX, double posY, double posZ, SpriteProvider spriteProvider) {
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

    @ClientOnly
    public record Factory(SpriteProvider spriteProvider) implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
            TelekineticShockParticle particle = new TelekineticShockParticle(clientWorld, posX, posY, posZ, spriteProvider);
            particle.setSpriteForAge(spriteProvider);
            return particle;
        }
    }
}

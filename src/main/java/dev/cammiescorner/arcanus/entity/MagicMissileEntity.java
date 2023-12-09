package dev.cammiescorner.arcanus.entity;

import dev.cammiescorner.arcanus.registry.ArcanusEntities;
import dev.cammiescorner.arcanus.registry.ArcanusParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.quiltmc.qsl.networking.api.PlayerLookup;

public class MagicMissileEntity extends AbstractArrow {

    public MagicMissileEntity(LivingEntity owner, Level world) {
        super(ArcanusEntities.MAGIC_MISSILE.get(), owner, world);
        setNoGravity(true);
        setBaseDamage(1.5D);
    }

    public MagicMissileEntity(EntityType<? extends MagicMissileEntity> type, Level world) {
        super(type, world);
        setNoGravity(true);
        setBaseDamage(1.5D);
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            for (int count = 0; count < 16; count++) {
                double x = getX() + (random.nextInt(3) - 1) / 4D;
                double y = getY() + 0.2F + (random.nextInt(3) - 1) / 4D;
                double z = getZ() + (random.nextInt(3) - 1) / 4D;
                double deltaX = (random.nextInt(3) - 1) * random.nextDouble();
                double deltaY = (random.nextInt(3) - 1) * random.nextDouble();
                double deltaZ = (random.nextInt(3) - 1) * random.nextDouble();

                PlayerLookup.tracking(this).forEach(player -> ((ServerLevel) level()).sendParticles(player, ArcanusParticles.MAGIC_MISSILE.get(), true, x, y, z, 1, deltaX, deltaY, deltaZ, 0.1));
            }
        }

        if (tickCount > 40)
            kill();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AMETHYST_BLOCK_STEP;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        kill();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);

        if (entityHitResult.getEntity() instanceof LivingEntity target)
            target.invulnerableTime = 0;
    }

    @Override
    public void playerTouch(Player player) {
        if (!level().isClientSide() && (inGround || isNoPhysics()) && shakeTime <= 0)
            discard();
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}

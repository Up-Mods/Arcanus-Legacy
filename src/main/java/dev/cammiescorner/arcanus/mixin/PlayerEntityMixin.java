package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.entity.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import dev.cammiescorner.arcanus.entity.MagicUser;
import dev.cammiescorner.arcanus.entity.SolarStrikeEntity;
import dev.cammiescorner.arcanus.registry.ArcanusParticles;
import dev.cammiescorner.arcanus.registry.ArcanusSoundEvents;
import dev.cammiescorner.arcanus.registry.ArcanusSpells;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.quiltmc.qsl.worldgen.dimension.api.QuiltDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicUser {

    @Unique
    private final List<Entity> hasHit = new ArrayList<>();
    @Unique
    private Spell activeSpell;
    @Unique
    private int spellTimer;

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(MANA_COST).add(MANA_REGEN).add(BURNOUT_REGEN).add(MANA_LOCK);
    }

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    @Shadow
    public abstract void sendMessage(Text message, boolean actionBar);

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient()) {
            MagicCaster caster = this.getComponent(ArcanusComponents.MAGIC_CASTER);
            if (activeSpell != null) {
                if (ArcanusSpells.LUNGE.equals(activeSpell))
                    castLunge();
                if (ArcanusSpells.DREAM_WARP.equals(activeSpell))
                    castDreamWarp();
                if (ArcanusSpells.MAGIC_MISSILE.equals(activeSpell))
                    castMagicMissile();
                if (ArcanusSpells.TELEKINESIS.equals(activeSpell))
                    castTelekinesis();
                if (ArcanusSpells.HEAL.equals(activeSpell))
                    castHeal();
                if (ArcanusSpells.DISCOMBOBULATE.equals(activeSpell))
                    castDiscombobulate();
                if (ArcanusSpells.SOLAR_STRIKE.equals(activeSpell))
                    castSolarStrike();
                if (ArcanusSpells.ARCANE_BARRIER.equals(activeSpell))
                    castArcaneBarrier();
            }

            if (spellTimer-- <= 0)
                spellTimer = 0;

            if (world.getTime() - caster.getLastCastTime() >= 20) {
                boolean dirty = false;
                int manaCooldown = (int) Math.round(ArcanusConfig.baseManaCooldown * getManaRegen((PlayerEntity) (Object) this));
                int burnoutCooldown = (int) Math.round(ArcanusConfig.baseBurnoutCooldown * getBurnoutRegen((PlayerEntity) (Object) this));

                if (manaCooldown != 0 && caster.getMana() < caster.getMaxMana() - caster.getBurnout() && world.getTime() % manaCooldown == 0) {
                    caster.addMana(1);
                    dirty = true;
                }

                if (burnoutCooldown != 0 && caster.getBurnout() > 0 && world.getTime() % burnoutCooldown == 0) {
                    caster.addBurnout(-1);
                    addExhaustion(5F);
                    dirty = true;
                }

                if (dirty) {
                    syncComponent(ArcanusComponents.MAGIC_CASTER);
                }
            }
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound tag, CallbackInfo info) {
        NbtCompound rootTag = tag.getCompound(Arcanus.MOD_ID);
        activeSpell = Arcanus.SPELL.get(new Identifier(rootTag.getString("ActiveSpell")));
        spellTimer = rootTag.getInt("SpellTimer");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound tag, CallbackInfo info) {
        NbtCompound rootTag = new NbtCompound();
        rootTag.putString("ActiveSpell", activeSpell != null ? Arcanus.SPELL.getId(activeSpell).toString() : "");
        rootTag.putInt("SpellTimer", spellTimer);
        tag.put(Arcanus.MOD_ID, rootTag);
    }

    @Override
    public void setActiveSpell(Spell spell, int timer) {
        this.activeSpell = spell;
        this.spellTimer = timer;
    }

    @Unique
    public void castLunge() {
        if (isFallFlying()) {
            if (spellTimer == 10)
                world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

            if (spellTimer > 0) {
                Vec3d rotation = getRotationVector();
                Vec3d velocity = getVelocity();
                float speed = 0.75F;

                setVelocity(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x), rotation.y * speed + (rotation.y * 1.5D - velocity.y), rotation.z * speed + (rotation.z * 1.5D - velocity.z)));

                world.getOtherEntities(null, getBoundingBox().expand(2)).forEach(entity -> {
                    if (entity != this && entity instanceof LivingEntity && !hasHit.contains(entity)) {
                        entity.damage(getDamageSources().playerAttack((PlayerEntity) (Object) this), 10);
                        hasHit.add(entity);
                    }
                });

                velocityModified = true;
            }

            if (isOnGround() || spellTimer <= 0) {
                activeSpell = null;
                hasHit.clear();
            }
        } else {
            if (spellTimer == 10) {
                setVelocity(0F, 0.75F, 0F);
                world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            }

            float adjustedPitch = MathHelper.abs(MathHelper.abs(getPitch() / 90F) - 1);

            if (spellTimer > 0) {
                addVelocity((getRotationVector().x * 0.025F + (getRotationVector().x - getVelocity().x)) * adjustedPitch, 0F, (getRotationVector().z * 0.025F + (getRotationVector().z - getVelocity().z)) * adjustedPitch);
                world.getOtherEntities(null, getBoundingBox().expand(2)).forEach(entity -> {
                    if (entity != this && entity instanceof LivingEntity && !hasHit.contains(entity)) {
                        entity.damage(getDamageSources().playerAttack((PlayerEntity) (Object) this), 10);
                        hasHit.add(entity);
                    }
                });

                velocityModified = true;
            }

            fallDistance = 0;

            if (isOnGround() && spellTimer <= 8) {
                spellTimer = 0;
                world.createExplosion(this, getX(), getY() + 0.5, getZ(), 1, World.ExplosionSourceType.NONE);
                activeSpell = null;
                hasHit.clear();
            }
        }
    }

    @Unique
    public void castDreamWarp() {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
        ServerWorld serverWorld = this.getServer().getWorld(serverPlayer.getSpawnPointDimension());
        BlockPos spawnPos = serverPlayer.getSpawnPointPosition();
        Vec3d rotation = this.getRotationVec(1F);
        float spawnAngle = serverPlayer.getSpawnAngle();
        boolean hasSpawnPoint = serverPlayer.isSpawnPointSet();

        activeSpell = null;

        if (serverWorld != null && spawnPos != null) {
            Vec3d spawnPoint = PlayerEntity.findRespawnPosition(serverWorld, spawnPos, spawnAngle, hasSpawnPoint, true).orElse(null);
            if (spawnPoint != null) {
                world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2F, 1F);
                QuiltDimensions.teleport(serverPlayer, serverWorld, new TeleportTarget(spawnPoint, Vec3d.ZERO, (float) rotation.x, (float) rotation.y));
                world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2F, 1F);
                return;
            }
        }

        sendMessage(Text.translatable("block.minecraft.spawn.not_valid"), false);
    }

    @Unique
    public void castMagicMissile() {
        MagicMissileEntity magicMissile = new MagicMissileEntity(this, world);
        magicMissile.setProperties(this, getPitch(), getYaw(), getRoll(), 4.5F, 0F);

        world.spawnEntity(magicMissile);
        world.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.MAGIC_MISSILE, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        activeSpell = null;
    }

    @Unique
    public void castTelekinesis() {
        HitResult result = ArcanusHelper.raycast(this, 10F, true);
        Vec3d rotation = getRotationVec(1F);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3d startPos = getCameraPosVec(1F).add((world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor);
            Vec3d endPos = result.getPos().add((world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, world, 0.5F, (ParticleEffect) ArcanusParticles.TELEKINETIC_SHOCK);
        }

        world.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.TELEKINETIC_SHOCK, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        switch (result.getType()) {
            case ENTITY -> {
                BlockPos pos = ((EntityHitResult) result).getEntity().getBlockPos();
                Box box = new Box(pos);

                world.getOtherEntities(this, box, EntityPredicates.VALID_ENTITY).forEach(target -> {
                    if (target instanceof PersistentProjectileEntity projectile)
                        projectile.fall();

                    target.setVelocity(rotation.multiply(2.5F));
                    target.velocityModified = true;
                });
            }
            case BLOCK -> {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();
                Box box = new Box(pos);
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof TntBlock) {
                    TntBlock.primeTnt(world, pos, this);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);

                    world.getEntitiesByClass(TntEntity.class, box, tnt -> tnt.isAlive() && tnt.getOwner() == this).forEach(target -> {
                        target.setVelocity(rotation.multiply(2.5F));
                        target.velocityModified = true;
                    });
                }

                if (block instanceof FallingBlock fallingBlock) {
                    FallingBlockEntity target = FallingBlockEntity.fall(world, pos, state);
                    fallingBlock.configureFallingBlockEntity(target);
                    target.setVelocity(rotation.multiply(2.5F));
                    target.velocityModified = true;
                    world.spawnEntity(target);
                }
            }
        }

        activeSpell = null;
    }

    @Unique
    public void castHeal() {
        heal(10);
        world.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.HEAL, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((random.nextInt(3) - 1) * random.nextFloat());
            float offsetY = random.nextFloat() * 2F;
            float offsetZ = ((random.nextInt(3) - 1) * random.nextFloat());

            ((ServerWorld) world).spawnParticles((ParticleEffect) ArcanusParticles.HEAL, getX() + offsetX, getY() - 0.5 + offsetY, getZ() + offsetZ, 1, 0, 0, 0, 0);
        }

        activeSpell = null;
    }

    @Unique
    public void castDiscombobulate() {
        HitResult result = ArcanusHelper.raycast(this, 4F, true);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3d startPos = getCameraPosVec(1F).add((world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor);
            Vec3d endPos = result.getPos().add((world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, world, 0.5F, (ParticleEffect) ArcanusParticles.DISCOMBOBULATE);
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) result).getEntity();
            ArcanusComponents.CAN_BE_DISCOMBOBULATED.maybeGet(target).ifPresent(component -> {
                component.setDiscombobulatedTimer(160);
                target.syncComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED);
            });
        } else {
            sendMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }

    @Unique
    public void castSolarStrike() {
        HitResult result = ArcanusHelper.raycast(this, 640F, false);

        if (result.getType() != HitResult.Type.MISS && result instanceof BlockHitResult blockHitResult) {
            ChunkPos chunkPos = new ChunkPos(blockHitResult.getBlockPos());
            ((ServerWorld) world).setChunkForced(chunkPos.x, chunkPos.z, true);
            SolarStrikeEntity solarStrike = new SolarStrikeEntity(this, world);
            solarStrike.setPosition(result.getPos());
            world.spawnEntity(solarStrike);
        } else {
            sendMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }

    @Unique
    public void castArcaneBarrier() {
        HitResult result = ArcanusHelper.raycast(this, 24F, false);

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockResult = ((BlockHitResult) result);
            Direction side = blockResult.getSide();
            ArcaneBarrierEntity arcaneWall = new ArcaneBarrierEntity((PlayerEntity) (Object) this, world);
            BlockPos pos = blockResult.getBlockPos().add(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ());
            arcaneWall.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.spawnEntity(arcaneWall);
        } else {
            sendMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }
}

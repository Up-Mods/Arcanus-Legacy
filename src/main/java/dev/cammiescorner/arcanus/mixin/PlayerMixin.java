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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MagicUser {

    @Unique
    private final List<Entity> hasHit = new ArrayList<>();
    @Unique
    private Spell activeSpell;
    @Unique
    private int spellTimer;

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        info.getReturnValue().add(MANA_COST).add(MANA_REGEN).add(BURNOUT_REGEN).add(MANA_LOCK);
    }

    @Shadow
    public abstract void causeFoodExhaustion(float exhaustion);

    @Shadow
    public abstract void displayClientMessage(Component message, boolean actionBar);

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!level.isClientSide()) {
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

            if (level.getGameTime() - caster.getLastCastTime() >= 20) {
                boolean dirty = false;
                int manaCooldown = (int) Math.round(ArcanusConfig.baseManaCooldown * getManaRegen((Player) (Object) this));
                int burnoutCooldown = (int) Math.round(ArcanusConfig.baseBurnoutCooldown * getBurnoutRegen((Player) (Object) this));

                if (manaCooldown != 0 && caster.getMana() < caster.getMaxMana() - caster.getBurnout() && level.getGameTime() % manaCooldown == 0) {
                    caster.addMana(1);
                    dirty = true;
                }

                if (burnoutCooldown != 0 && caster.getBurnout() > 0 && level.getGameTime() % burnoutCooldown == 0) {
                    caster.addBurnout(-1);
                    causeFoodExhaustion(5F);
                    dirty = true;
                }

                if (dirty) {
                    syncComponent(ArcanusComponents.MAGIC_CASTER);
                }
            }
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readNbt(CompoundTag tag, CallbackInfo info) {
        CompoundTag rootTag = tag.getCompound(Arcanus.MOD_ID);
        activeSpell = Arcanus.SPELL.get(new ResourceLocation(rootTag.getString("ActiveSpell")));
        spellTimer = rootTag.getInt("SpellTimer");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void writeNbt(CompoundTag tag, CallbackInfo info) {
        CompoundTag rootTag = new CompoundTag();
        rootTag.putString("ActiveSpell", activeSpell != null ? Arcanus.SPELL.getKey(activeSpell).toString() : "");
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
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

            if (spellTimer > 0) {
                Vec3 rotation = getLookAngle();
                Vec3 velocity = getDeltaMovement();
                float speed = 0.75F;

                setDeltaMovement(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x), rotation.y * speed + (rotation.y * 1.5D - velocity.y), rotation.z * speed + (rotation.z * 1.5D - velocity.z)));

                level.getEntities(null, getBoundingBox().inflate(2)).forEach(entity -> {
                    if (entity != this && entity instanceof LivingEntity && !hasHit.contains(entity)) {
                        entity.hurt(damageSources().playerAttack((Player) (Object) this), 10);
                        hasHit.add(entity);
                    }
                });

                hurtMarked = true;
            }

            if (isOnGround() || spellTimer <= 0) {
                activeSpell = null;
                hasHit.clear();
            }
        } else {
            if (spellTimer == 10) {
                setDeltaMovement(0F, 0.75F, 0F);
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
            }

            float adjustedPitch = Mth.abs(Mth.abs(getXRot() / 90F) - 1);

            if (spellTimer > 0) {
                push((getLookAngle().x * 0.025F + (getLookAngle().x - getDeltaMovement().x)) * adjustedPitch, 0F, (getLookAngle().z * 0.025F + (getLookAngle().z - getDeltaMovement().z)) * adjustedPitch);
                level.getEntities(null, getBoundingBox().inflate(2)).forEach(entity -> {
                    if (entity != this && entity instanceof LivingEntity && !hasHit.contains(entity)) {
                        entity.hurt(damageSources().playerAttack((Player) (Object) this), 10);
                        hasHit.add(entity);
                    }
                });

                hurtMarked = true;
            }

            fallDistance = 0;

            if (isOnGround() && spellTimer <= 8) {
                spellTimer = 0;
                level.explode(this, getX(), getY() + 0.5, getZ(), 1, Level.ExplosionInteraction.NONE);
                activeSpell = null;
                hasHit.clear();
            }
        }
    }

    @Unique
    public void castDreamWarp() {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        ServerLevel serverWorld = this.getServer().getLevel(serverPlayer.getRespawnDimension());
        BlockPos spawnPos = serverPlayer.getRespawnPosition();
        Vec3 rotation = this.getViewVector(1F);
        float spawnAngle = serverPlayer.getRespawnAngle();
        boolean hasSpawnPoint = serverPlayer.isRespawnForced();

        activeSpell = null;

        if (serverWorld != null && spawnPos != null) {
            Vec3 spawnPoint = Player.findRespawnPositionAndUseSpawnBlock(serverWorld, spawnPos, spawnAngle, hasSpawnPoint, true).orElse(null);
            if (spawnPoint != null) {
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2F, 1F);
                QuiltDimensions.teleport(serverPlayer, serverWorld, new PortalInfo(spawnPoint, Vec3.ZERO, (float) rotation.x, (float) rotation.y));
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2F, 1F);
                return;
            }
        }

        displayClientMessage(Component.translatable("block.minecraft.spawn.not_valid"), false);
    }

    @Unique
    public void castMagicMissile() {
        MagicMissileEntity magicMissile = new MagicMissileEntity(this, level);
        magicMissile.shootFromRotation(this, getXRot(), getYRot(), getFallFlyingTicks(), 4.5F, 0F);

        level.addFreshEntity(magicMissile);
        level.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.MAGIC_MISSILE, SoundSource.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        activeSpell = null;
    }

    @Unique
    public void castTelekinesis() {
        HitResult result = ArcanusHelper.raycast(this, 10F, true);
        Vec3 rotation = getViewVector(1F);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3 startPos = getEyePosition(1F).add((level.random.nextInt(3) - 1) / startDivisor, (level.random.nextInt(3) - 1) / startDivisor, (level.random.nextInt(3) - 1) / startDivisor);
            Vec3 endPos = result.getLocation().add((level.random.nextInt(3) - 1) / endDivisor, (level.random.nextInt(3) - 1) / endDivisor, (level.random.nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, level, 0.5F, (ParticleOptions) ArcanusParticles.TELEKINETIC_SHOCK);
        }

        level.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.TELEKINETIC_SHOCK, SoundSource.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        switch (result.getType()) {
            case ENTITY -> {
                BlockPos pos = ((EntityHitResult) result).getEntity().blockPosition();
                AABB box = new AABB(pos);

                level.getEntities(this, box, EntitySelector.ENTITY_STILL_ALIVE).forEach(target -> {
                    if (target instanceof AbstractArrow projectile)
                        projectile.startFalling();

                    target.setDeltaMovement(rotation.scale(2.5F));
                    target.hurtMarked = true;
                });
            }
            case BLOCK -> {
                BlockPos pos = ((BlockHitResult) result).getBlockPos();
                AABB box = new AABB(pos);
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                if (block instanceof TntBlock) {
                    TntBlock.explode(level, pos, this);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);

                    level.getEntitiesOfClass(PrimedTnt.class, box, tnt -> tnt.isAlive() && tnt.getOwner() == this).forEach(target -> {
                        target.setDeltaMovement(rotation.scale(2.5F));
                        target.hurtMarked = true;
                    });
                }

                if (block instanceof FallingBlock fallingBlock) {
                    FallingBlockEntity target = FallingBlockEntity.fall(level, pos, state);
                    fallingBlock.falling(target);
                    target.setDeltaMovement(rotation.scale(2.5F));
                    target.hurtMarked = true;
                    level.addFreshEntity(target);
                }
            }
        }

        activeSpell = null;
    }

    @Unique
    public void castHeal() {
        heal(10);
        level.playSound(null, getX(), getY(), getZ(), ArcanusSoundEvents.HEAL, SoundSource.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

        for (int amount = 0; amount < 32; amount++) {
            float offsetX = ((random.nextInt(3) - 1) * random.nextFloat());
            float offsetY = random.nextFloat() * 2F;
            float offsetZ = ((random.nextInt(3) - 1) * random.nextFloat());

            ((ServerLevel) level).sendParticles((ParticleOptions) ArcanusParticles.HEAL, getX() + offsetX, getY() - 0.5 + offsetY, getZ() + offsetZ, 1, 0, 0, 0, 0);
        }

        activeSpell = null;
    }

    @Unique
    public void castDiscombobulate() {
        HitResult result = ArcanusHelper.raycast(this, 4F, true);
        double startDivisor = 5D;
        double endDivisor = 15D;

        for (int count = 0; count < 8; count++) {
            Vec3 startPos = getEyePosition(1F).add((level.random.nextInt(3) - 1) / startDivisor, (level.random.nextInt(3) - 1) / startDivisor, (level.random.nextInt(3) - 1) / startDivisor);
            Vec3 endPos = result.getLocation().add((level.random.nextInt(3) - 1) / endDivisor, (level.random.nextInt(3) - 1) / endDivisor, (level.random.nextInt(3) - 1) / endDivisor);

            ArcanusHelper.drawLine(startPos, endPos, level, 0.5F, (ParticleOptions) ArcanusParticles.DISCOMBOBULATE);
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult) result).getEntity();
            ArcanusComponents.CAN_BE_DISCOMBOBULATED.maybeGet(target).ifPresent(component -> {
                component.setDiscombobulatedTimer(160);
                target.syncComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED);
            });
        } else {
            displayClientMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }

    @Unique
    public void castSolarStrike() {
        HitResult result = ArcanusHelper.raycast(this, 640F, false);

        if (result.getType() != HitResult.Type.MISS && result instanceof BlockHitResult blockHitResult) {
            ChunkPos chunkPos = new ChunkPos(blockHitResult.getBlockPos());
            ((ServerLevel) level).setChunkForced(chunkPos.x, chunkPos.z, true);
            SolarStrikeEntity solarStrike = new SolarStrikeEntity(this, level);
            solarStrike.setPos(result.getLocation());
            level.addFreshEntity(solarStrike);
        } else {
            displayClientMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }

    @Unique
    public void castArcaneBarrier() {
        HitResult result = ArcanusHelper.raycast(this, 24F, false);

        if (result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockResult = ((BlockHitResult) result);
            Direction side = blockResult.getDirection();
            ArcaneBarrierEntity arcaneWall = new ArcaneBarrierEntity((Player) (Object) this, level);
            BlockPos pos = blockResult.getBlockPos().offset(side.getStepX(), side.getStepY(), side.getStepZ());
            arcaneWall.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            level.addFreshEntity(arcaneWall);
        } else {
            displayClientMessage(Arcanus.translate("spell", "no_target"), false);
        }

        activeSpell = null;
    }
}

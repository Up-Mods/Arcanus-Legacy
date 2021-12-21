package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.ArcaneBarrierEntity;
import dev.cammiescorner.arcanus.common.entities.MagicMissileEntity;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import dev.cammiescorner.arcanus.core.registry.ModParticles;
import dev.cammiescorner.arcanus.core.registry.ModSoundEvents;
import dev.cammiescorner.arcanus.core.registry.ModSpells;
import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import dev.cammiescorner.arcanus.core.util.CanBeDiscombobulated;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.cammiescorner.arcanus.Arcanus.DataTrackers.*;
import static dev.cammiescorner.arcanus.Arcanus.EntityAttributes.*;
import static dev.cammiescorner.arcanus.Arcanus.getConfig;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicUser {
	@Shadow public abstract void addExhaustion(float exhaustion);
	@Shadow public abstract void sendMessage(Text message, boolean actionBar);

	@Shadow protected HungerManager hungerManager;
	@Unique private final List<Spell> knownSpells = new ArrayList<>(8);
	@Unique private Spell activeSpell = null;
	@Unique private long lastCastTime = 0;
	@Unique private int spellTimer = 0;
	@Unique private static final int MAX_MANA = 20;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "createPlayerAttributes", at = @At("RETURN"))
	private static void createPlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
		info.getReturnValue().add(MANA_COST).add(MANA_REGEN).add(BURNOUT_REGEN).add(MANA_LOCK);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info) {
		if(!world.isClient()) {
			if(getMana() > getMaxMana())
				setMana(getMana());
			if(getBurnout() > getMaxBurnout())
				setBurnout(getBurnout());

			if(activeSpell != null) {
				if(ModSpells.LUNGE.equals(activeSpell))
					castLunge();
				if(ModSpells.DREAM_WARP.equals(activeSpell))
					castDreamWarp();
				if(ModSpells.MAGIC_MISSILE.equals(activeSpell))
					castMagicMissile();
				if(ModSpells.TELEKINESIS.equals(activeSpell))
					castTelekinesis();
				if(ModSpells.HEAL.equals(activeSpell))
					castHeal();
				if(ModSpells.DISCOMBOBULATE.equals(activeSpell))
					castDiscombobulate();
				if(ModSpells.SOLAR_STRIKE.equals(activeSpell))
					castSolarStrike();
				if(ModSpells.ARCANE_BARRIER.equals(activeSpell))
					castArcaneBarrier();
			}

			if(spellTimer-- <= 0)
				spellTimer = 0;

			if(world.getTime() >= lastCastTime + 20) {
				int manaCooldown = (int) Math.round(getConfig().baseManaCooldown * ArcanusHelper.getManaRegen((PlayerEntity) (Object) this));
				int burnoutCooldown = (int) Math.round(getConfig().baseBurnoutCooldown * ArcanusHelper.getBurnoutRegen((PlayerEntity) (Object) this));

				if(manaCooldown != 0 && getMana() < getMaxMana() - getBurnout() && world.getTime() % manaCooldown == 0)
					addMana(1);

				if(burnoutCooldown != 0 && getBurnout() > 0 && hungerManager.getFoodLevel() > 0 && world.getTime() % burnoutCooldown == 0) {
					addBurnout(-1);
					addExhaustion(5F);
				}
			}
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound tag, CallbackInfo info) {
		NbtCompound rootTag = tag.getCompound(Arcanus.MOD_ID);
		NbtList listTag = rootTag.getList("KnownSpells", NbtType.STRING);

		for(int i = 0; i < listTag.size(); i++)
			Arcanus.SPELL.getOrEmpty(new Identifier(listTag.getString(i))).ifPresent(knownSpells::add);

		dataTracker.set(MANA, rootTag.getInt("Mana"));
		dataTracker.set(BURNOUT, rootTag.getInt("Burnout"));
		dataTracker.set(SHOW_MANA, rootTag.getBoolean("ShowMana"));
		activeSpell = Arcanus.SPELL.get(new Identifier(rootTag.getString("ActiveSpell")));
		lastCastTime = rootTag.getLong("LastCastTime");
		spellTimer = rootTag.getInt("SpellTimer");
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound tag, CallbackInfo info) {
		NbtCompound rootTag = new NbtCompound();
		NbtList listTag = new NbtList();

		tag.put(Arcanus.MOD_ID, rootTag);
		knownSpells.forEach(spell -> listTag.add(NbtString.of(Arcanus.SPELL.getId(spell).toString())));
		rootTag.put("KnownSpells", listTag);
		rootTag.putInt("Mana", dataTracker.get(MANA));
		rootTag.putInt("Burnout", dataTracker.get(BURNOUT));
		rootTag.putBoolean("ShowMana", dataTracker.get(SHOW_MANA));
		rootTag.putString("ActiveSpell", activeSpell != null ? Arcanus.SPELL.getId(activeSpell).toString() : "");
		rootTag.putLong("LastCastTime", lastCastTime);
		rootTag.putInt("SpellTimer", spellTimer);
	}

	@Inject(method = "initDataTracker", at = @At("HEAD"))
	public void initTracker(CallbackInfo info) {
		dataTracker.startTracking(MANA, MAX_MANA);
		dataTracker.startTracking(BURNOUT, 0);
		dataTracker.startTracking(SHOW_MANA, false);
	}

	@Override
	public List<Spell> getKnownSpells() {
		return knownSpells;
	}

	@Override
	public void setKnownSpell(Identifier spellId) {
		Spell spell = Arcanus.SPELL.get(spellId);

		if(!knownSpells.contains(spell))
			knownSpells.add(spell);
		else if(spell != null)
			Arcanus.LOGGER.warn("Spell " + spell.getTranslationKey() + " is already known!");
	}

	@Override
	public int getMana() {
		return dataTracker.get(MANA);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA - ArcanusHelper.getManaLock((PlayerEntity) (Object) this);
	}

	@Override
	public void setMana(int amount) {
		dataTracker.set(MANA, MathHelper.clamp(amount, 0, getMaxMana()));
	}

	@Override
	public void addMana(int amount) {
		setMana(Math.min(getMana() + amount, getMaxMana()));
	}

	@Override
	public int getBurnout() {
		return dataTracker.get(BURNOUT);
	}

	@Override
	public int getMaxBurnout() {
		return getMaxMana();
	}

	@Override
	public void setBurnout(int amount) {
		dataTracker.set(BURNOUT, MathHelper.clamp(amount, 0, getMaxBurnout()));
	}

	@Override
	public void addBurnout(int amount) {
		setBurnout(Math.min(getBurnout() + amount, getMaxBurnout()));
	}

	public void setManaLock(int amount) {
		setMana(getMana());
		setBurnout(getBurnout());
	}

	@Override
	public boolean isManaVisible() {
		return dataTracker.get(SHOW_MANA);
	}

	@Override
	public void shouldShowMana(boolean shouldShowMana) {
		dataTracker.set(SHOW_MANA, shouldShowMana);
	}

	@Override
	public void setLastCastTime(long lastCastTime) {
		this.lastCastTime = lastCastTime;
	}

	@Override
	public void setActiveSpell(Spell spell, int timer) {
		this.activeSpell = spell;
		this.spellTimer = timer;
	}

	@Unique
	public void castLunge() {
		if(isFallFlying()) {
			if(spellTimer == 10)
				world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

			if(spellTimer > 0) {
				Vec3d rotation = getRotationVector();
				Vec3d velocity = getVelocity();
				float speed = 0.75F;

				setVelocity(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x), rotation.y * speed + (rotation.y * 1.5D - velocity.y), rotation.z * speed + (rotation.z * 1.5D - velocity.z)));

				world.getOtherEntities(null, getBoundingBox().expand(2)).forEach(entity -> {
					if(entity != this && entity instanceof LivingEntity)
						entity.damage(DamageSource.player((PlayerEntity) (Object) this), 10);
				});

				velocityModified = true;
			}

			if(isOnGround() || spellTimer <= 0)
				activeSpell = null;
		}
		else {
			if(spellTimer == 10) {
				setVelocity(0F, 0.75F, 0F);
				world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			}

			float adjustedPitch = MathHelper.abs(MathHelper.abs(getPitch() / 90F) - 1);

			if(spellTimer > 0) {
				addVelocity((getRotationVector().x * 0.025F + (getRotationVector().x - getVelocity().x)) * adjustedPitch, 0F, (getRotationVector().z * 0.025F + (getRotationVector().z - getVelocity().z)) * adjustedPitch);
				world.getOtherEntities(null, getBoundingBox().expand(2)).forEach(entity -> {
					if(entity != this && entity instanceof LivingEntity)
						entity.damage(DamageSource.player((PlayerEntity) (Object) this), 10);
				});

				velocityModified = true;
			}

			fallDistance = 0;

			if(isOnGround() && spellTimer <= 8) {
				spellTimer = 0;
				world.createExplosion(this, getX(), getY() + 0.5, getZ(), 1, Explosion.DestructionType.NONE);
				activeSpell = null;
			}
		}
	}

	@Unique
	public void castDreamWarp() {
		ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
		ServerWorld serverWorld = serverPlayer.getServer().getWorld(serverPlayer.getSpawnPointDimension());
		BlockPos spawnPos = serverPlayer.getSpawnPointPosition();
		Vec3d rotation = serverPlayer.getRotationVec(1F);
		Optional<Vec3d> optionalSpawnPoint;
		float spawnAngle = serverPlayer.getSpawnAngle();
		boolean hasSpawnPoint = serverPlayer.isSpawnForced();

		if(serverWorld != null && spawnPos != null)
			optionalSpawnPoint = PlayerEntity.findRespawnPosition(serverWorld, spawnPos, spawnAngle, hasSpawnPoint, true);
		else
			optionalSpawnPoint = Optional.empty();

		if(optionalSpawnPoint.isPresent()) {
			Vec3d spawnPoint = optionalSpawnPoint.get();
			System.out.println(spawnPoint);
			world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2F, 1F);
			TeleportTarget target = new TeleportTarget(
					new Vec3d(spawnPoint.x, spawnPoint.y, spawnPoint.z),
					new Vec3d(0, 0, 0),
					(float) rotation.x,
					(float) rotation.y
			);
			doTeleport(serverPlayer, serverWorld, target);
			world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 2F, 1F);
		}
		else {
			sendMessage(new TranslatableText("block.minecraft.spawn.not_valid"), false);
		}

		activeSpell = null;
	}

	@Unique
	private void doTeleport(ServerPlayerEntity player, ServerWorld world, TeleportTarget target) {
		if (player.world.getRegistryKey().equals(world.getRegistryKey())) {
			player.networkHandler.requestTeleport(
					target.position.getX(),
					target.position.getY(),
					target.position.getZ(),
					target.yaw,
					target.pitch
			);
		} else {
			FabricDimensions.teleport(player, world, target);
		}
	}

	@Unique
	public void castMagicMissile() {
		MagicMissileEntity magicMissile = new MagicMissileEntity(this, world);
		magicMissile.setVelocity(this, getPitch(), getYaw(), getRoll(), 4.5F, 0F);

		world.spawnEntity(magicMissile);
		world.playSound(null, getBlockPos(), ModSoundEvents.MAGIC_MISSILE, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
		activeSpell = null;
	}

	@Unique
	public void castTelekinesis() {
		HitResult result = ArcanusHelper.raycast(this, 10F, true);
		Vec3d rotation = getRotationVec(1F);
		double startDivisor = 5D;
		double endDivisor = 15D;

		for(int count = 0; count < 8; count++) {
			Vec3d startPos = getCameraPosVec(1F).add((world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor);
			Vec3d endPos = result.getPos().add((world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor);

			ArcanusHelper.drawLine(startPos, endPos, world, 0.5F, (ParticleEffect) ModParticles.TELEKINETIC_SHOCK);
		}

		world.playSound(null, getBlockPos(), ModSoundEvents.TELEKINETIC_SHOCK, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

		switch(result.getType()) {
			case ENTITY -> {
				BlockPos pos = ((EntityHitResult) result).getEntity().getBlockPos();
				Box box = new Box(pos);

				world.getOtherEntities(this, box, EntityPredicates.VALID_ENTITY).forEach(target -> {
					if(target instanceof PersistentProjectileEntity projectile)
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

				if(block instanceof TntBlock) {
					TntBlock.primeTnt(world, pos, this);
					world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);

					world.getEntitiesByClass(TntEntity.class, box, tnt -> tnt.isAlive() && tnt.getCausingEntity() == this).forEach(target -> {
						target.setVelocity(rotation.multiply(2.5F));
						target.velocityModified = true;
					});
				}

				if(block instanceof FallingBlock fallingBlock) {
					FallingBlockEntity target = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);
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
		world.playSound(null, getBlockPos(), ModSoundEvents.HEAL, SoundCategory.PLAYERS, 2F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

		for(int amount = 0; amount < 32; amount++) {
			float offsetX = ((random.nextInt(3) - 1) * random.nextFloat());
			float offsetY = random.nextFloat() * 2F;
			float offsetZ = ((random.nextInt(3) - 1) * random.nextFloat());

			((ServerWorld) world).spawnParticles((ParticleEffect) ModParticles.HEAL, getX() + offsetX, getY() - 0.5 + offsetY, getZ() + offsetZ, 1, 0, 0, 0, 0);
		}

		activeSpell = null;
	}

	@Unique
	public void castDiscombobulate() {
		HitResult result = ArcanusHelper.raycast(this, 4F, true);
		double startDivisor = 5D;
		double endDivisor = 15D;

		for(int count = 0; count < 8; count++) {
			Vec3d startPos = getCameraPosVec(1F).add((world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor, (world.random.nextInt(3) - 1) / startDivisor);
			Vec3d endPos = result.getPos().add((world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor, (world.random.nextInt(3) - 1) / endDivisor);

			ArcanusHelper.drawLine(startPos, endPos, world, 0.5F, (ParticleEffect) ModParticles.DISCOMBOBULATE);
		}

		if(result.getType() == HitResult.Type.ENTITY) {
			if(((EntityHitResult) result).getEntity() instanceof CanBeDiscombobulated target) {
				target.setDiscombobulated(true);
				target.setDiscombobulatedTimer(160);
			}
		}
		else {
			sendMessage(new TranslatableText("spell." + Arcanus.MOD_ID + ".no_target"), false);
		}

		activeSpell = null;
	}

	@Unique
	public void castSolarStrike() {
		HitResult result = ArcanusHelper.raycast(this, 640F, false);

		if(result.getType() != HitResult.Type.MISS) {
			ChunkPos chunkPos = new ChunkPos(new BlockPos(result.getPos()));
			((ServerWorld) world).setChunkForced(chunkPos.x, chunkPos.z, true);
			SolarStrikeEntity solarStrike = new SolarStrikeEntity(this, world);
			solarStrike.setPosition(result.getPos());
			world.spawnEntity(solarStrike);
		}
		else {
			sendMessage(new TranslatableText("spell." + Arcanus.MOD_ID + ".no_target"), false);
		}

		activeSpell = null;
	}

	@Unique
	public void castArcaneBarrier() {
		HitResult result = ArcanusHelper.raycast(this, 24F, false);

		if(result.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockResult = ((BlockHitResult) result);
			Direction side = blockResult.getSide();
			ArcaneBarrierEntity arcaneWall = new ArcaneBarrierEntity((PlayerEntity) (Object) this, world);
			BlockPos pos = blockResult.getBlockPos().add(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ());
			arcaneWall.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			world.spawnEntity(arcaneWall);
		}
		else {
			sendMessage(new TranslatableText("spell." + Arcanus.MOD_ID + ".no_target"), false);
		}

		activeSpell = null;
	}
}

package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.entities.SolarStrikeEntity;
import dev.cammiescorner.arcanus.core.registry.ModSpells;
import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static dev.cammiescorner.arcanus.Arcanus.config;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicUser {
	@Shadow
	public abstract void addExhaustion(float exhaustion);

	@Shadow
	protected HungerManager hungerManager;
	@Unique
	private final List<Spell> knownSpells = new ArrayList<>(8);
	@Unique
	private Spell activeSpell = null;
	@Unique
	private long lastCastTime = 0;
	@Unique
	private int spellTimer = 0;
	@Unique
	private static final int MAX_MANA = 20;
	@Unique
	private static final int MAX_BURNOUT = 20;
	@Unique
	private static final TrackedData<Integer> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> BURNOUT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info) {
		if(!world.isClient()) {
			if(activeSpell != null) {
				if(ModSpells.LUNGE.equals(activeSpell))
					castLunge();
				if(ModSpells.FISSURE.equals(activeSpell))
					castFissure();
				if(ModSpells.MAGIC_MISSILE.equals(activeSpell))
					castMagicMissile();
				if(ModSpells.VANISH.equals(activeSpell))
					castVanish();
				if(ModSpells.HEAL.equals(activeSpell))
					castHeal();
				if(ModSpells.METEOR.equals(activeSpell))
					castMeteor();
				if(ModSpells.SOLAR_STRIKE.equals(activeSpell))
					castSolarStrike();
				if(ModSpells.MINE.equals(activeSpell))
					castMine();
			}

			if(spellTimer-- <= 0)
				spellTimer = 0;

			if(world.getTime() >= lastCastTime + 20) {
				if(getMana() < getMaxMana() - getBurnout() && world.getTime() % config.manaCooldown == 0)
					addMana(1);

				if(getBurnout() > 0 && hungerManager.getFoodLevel() > 0 && world.getTime() % config.burnoutCooldown == 0) {
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
			knownSpells.add(Arcanus.SPELL.get(new Identifier(listTag.getString(i))));

		dataTracker.set(MANA, rootTag.getInt("Mana"));
		dataTracker.set(BURNOUT, rootTag.getInt("Burnout"));
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
		rootTag.putString("ActiveSpell", activeSpell != null ? Arcanus.SPELL.getId(activeSpell).toString() : "");
		rootTag.putLong("LastCastTime", lastCastTime);
		rootTag.putInt("SpellTimer", spellTimer);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initTracker(CallbackInfo info) {
		dataTracker.startTracking(MANA, MAX_MANA);
		dataTracker.startTracking(BURNOUT, 0);
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
		else
			Arcanus.LOGGER.warn("Spell " + spell.getTranslationKey() + " is already known!");
	}

	@Override
	public int getMana() {
		return dataTracker.get(MANA);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}

	@Override
	public void setMana(int amount) {
		dataTracker.set(MANA, MathHelper.clamp(amount, 0, MAX_MANA));
	}

	@Override
	public void addMana(int amount) {
		setMana(Math.min(getMana() + amount, MAX_MANA));
	}

	@Override
	public int getBurnout() {
		return dataTracker.get(BURNOUT);
	}

	@Override
	public int getMaxBurnout() {
		return MAX_BURNOUT;
	}

	@Override
	public void setBurnout(int amount) {
		dataTracker.set(BURNOUT, MathHelper.clamp(amount, 0, MAX_BURNOUT));
	}

	@Override
	public void addBurnout(int amount) {
		setBurnout(Math.min(getBurnout() + amount, MAX_BURNOUT));
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
		if(spellTimer == 10)
			setVelocity(0F, 0.75F, 0F);

		float adjustedPitch = MathHelper.abs(MathHelper.abs(getPitch() / 90F) - 1);

		if(spellTimer > 0) {
			addVelocity((getRotationVector().x * 0.025F + (getRotationVector().x - getVelocity().x)) * adjustedPitch, 0F, (getRotationVector().z * 0.025F + (getRotationVector().z - getVelocity().z)) * adjustedPitch);
			world.getOtherEntities(null, getBoundingBox().expand(2)).forEach(entity -> {
				if(entity != this && entity instanceof LivingEntity)
					entity.damage(DamageSource.player((PlayerEntity) (Object) this), 5);
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

	@Unique
	public void castFissure() {
		activeSpell = null;
	}

	@Unique
	public void castMagicMissile() {
		activeSpell = null;
	}

	@Unique
	public void castVanish() {
		activeSpell = null;
	}

	@Unique
	public void castHeal() {
		heal(4);
		activeSpell = null;
	}

	@Unique
	public void castMeteor() {
		activeSpell = null;
	}

	@Unique
	public void castSolarStrike() {
		HitResult result = ArcanusHelper.raycast(this, 256F);

		if(result.getType() != HitResult.Type.MISS) {
			SolarStrikeEntity solarStrikeEntity = new SolarStrikeEntity(this, world);
			solarStrikeEntity.setPosition(result.getPos());
			world.spawnEntity(solarStrikeEntity);
		}

		activeSpell = null;
	}

	@Unique
	public void castMine() {
		activeSpell = null;
	}
}

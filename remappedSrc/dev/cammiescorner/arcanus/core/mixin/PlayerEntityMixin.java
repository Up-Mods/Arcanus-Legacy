package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static dev.cammiescorner.arcanus.Arcanus.*;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicUser
{
	@Shadow
	public abstract void addExhaustion(float exhaustion);

	@Unique
	private List<Spell> knownSpells = new ArrayList<>(8);
	@Unique
	private long lastCastTime = 0;
	@Unique
	private static final int MAX_MANA = 20;
	@Unique
	private static final int MAX_BURNOUT = 20;
	@Unique
	private static final EntityDataAccessor<Integer> MANA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
	@Unique
	private static final EntityDataAccessor<Integer> BURNOUT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) { super(entityType, world); }

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info)
	{
		if(!level.isClientSide() && level.getGameTime() >= lastCastTime + 20)
		{
			if(getMana() < getMaxMana() - getBurnout() && level.getGameTime() % config.manaCooldown == 0)
			{
				addMana(1);
			}

			if(getBurnout() > 0 && level.getGameTime() % config.burnoutCooldown == 0)
			{
				addBurnout(-1);
				addExhaustion(5F);
			}
		}
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readNbt(CompoundTag tag, CallbackInfo info)
	{
		CompoundTag rootTag = tag.getCompound(Arcanus.MOD_ID);
		ListTag listTag = rootTag.getList("KnownSpells", NbtType.STRING);

		for(int i = 0; i < listTag.size(); i++)
			knownSpells.add(Arcanus.SPELL.get(new ResourceLocation(listTag.getString(i))));

		entityData.set(MANA, tag.getInt("Mana"));
		entityData.set(BURNOUT, tag.getInt("Burnout"));
		lastCastTime = tag.getLong("LastCastTime");
	}

	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeNbt(CompoundTag tag, CallbackInfo info)
	{
		CompoundTag rootTag = new CompoundTag();
		ListTag listTag = new ListTag();

		knownSpells.forEach(spell -> listTag.add(StringTag.of(Arcanus.SPELL.getKey(spell).toString())));
		rootTag.put("KnownSpells", listTag);
		tag.put(Arcanus.MOD_ID, rootTag);
		tag.putInt("Mana", entityData.get(MANA));
		tag.putInt("Burnout", entityData.get(BURNOUT));
		tag.putLong("LastCastTime", lastCastTime);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initTracker(CallbackInfo info)
	{
		entityData.define(MANA, MAX_MANA);
		entityData.define(BURNOUT, 0);
	}

	@Override
	public List<Spell> getKnownSpells()
	{
		return knownSpells;
	}

	@Override
	public void setKnownSpell(ResourceLocation spellId)
	{
		Spell spell = Arcanus.SPELL.get(spellId);

		if(!knownSpells.contains(spell))
			knownSpells.add(spell);
		else
			Arcanus.LOGGER.warn("Spell " + spellId.toString() + " is already known!");
	}

	@Override
	public int getMana()
	{
		return entityData.get(MANA);
	}

	@Override
	public int getMaxMana()
	{
		return MAX_MANA;
	}

	@Override
	public void setMana(int amount)
	{
		entityData.set(MANA, Mth.clamp(amount, 0, MAX_MANA));
	}

	@Override
	public void addMana(int amount)
	{
		setMana(Math.min(getMana() + amount, MAX_MANA));
	}

	@Override
	public int getBurnout()
	{
		return entityData.get(BURNOUT);
	}

	@Override
	public int getMaxBurnout()
	{
		return MAX_BURNOUT;
	}

	@Override
	public void setBurnout(int amount)
	{
		entityData.set(BURNOUT, Mth.clamp(amount, 0, MAX_BURNOUT));
	}

	@Override
	public void addBurnout(int amount)
	{
		setBurnout(Math.min(getBurnout() + amount, MAX_BURNOUT));
	}

	@Override
	public void setLastCastTime(long lastCastTime)
	{
		this.lastCastTime = lastCastTime;
	}
}

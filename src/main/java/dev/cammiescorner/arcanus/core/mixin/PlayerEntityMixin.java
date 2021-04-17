package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements MagicUser
{
	@Unique
	private List<Spell> knownSpells = new ArrayList<>(16);
	@Unique
	private long lastCastTime = 0;
	@Unique
	private static final TrackedData<Integer> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final int MAX_MANA = 64;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info)
	{
		if(!world.isClient())
			if(dataTracker.get(MANA) < MAX_MANA && world.getTime() >= lastCastTime + 40 && world.getTime() % 2 == 0)
				dataTracker.set(MANA, dataTracker.get(MANA) + 1);
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void readNbt(CompoundTag tag, CallbackInfo info)
	{
		CompoundTag rootTag = tag.getCompound(Arcanus.MOD_ID);
		ListTag listTag = rootTag.getList("KnownSpells", NbtType.STRING);

		for(int i = 0; i < listTag.size(); i++)
			knownSpells.add(Arcanus.SPELL.get(new Identifier(listTag.getString(i))));

		dataTracker.set(MANA, tag.getInt("Mana"));
		lastCastTime = tag.getLong("LastCastTime");
	}

	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void writeNbt(CompoundTag tag, CallbackInfo info)
	{
		CompoundTag rootTag = new CompoundTag();
		ListTag listTag = new ListTag();

		knownSpells.forEach(spell -> listTag.add(StringTag.of(Arcanus.SPELL.getId(spell).toString())));
		rootTag.put("KnownSpells", listTag);
		tag.put(Arcanus.MOD_ID, rootTag);
		tag.putInt("Mana", dataTracker.get(MANA));
		tag.putLong("LastCastTime", lastCastTime);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void initTracker(CallbackInfo info)
	{
		dataTracker.startTracking(MANA, 0);
	}

	@Override
	public List<Spell> getKnownSpells()
	{
		return knownSpells;
	}

	@Override
	public void setKnownSpell(Identifier spellId)
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
		return dataTracker.get(MANA);
	}

	@Override
	public int getMaxMana()
	{
		return MAX_MANA;
	}

	@Override
	public void setMana(int amount)
	{
		dataTracker.set(MANA, MathHelper.clamp(amount, 0, MAX_MANA));
	}

	@Override
	public void addMana(int amount)
	{
		setMana(Math.min(getMana() + amount, MAX_MANA));
	}

	@Override
	public void setLastCastTime(long lastCastTime)
	{
		this.lastCastTime = lastCastTime;
	}
}

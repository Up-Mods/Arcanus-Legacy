package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class AuraComponent implements AutoSyncedComponent, ServerTickingComponent {
	private static final int MAX_AURA = 20;
	private final PlayerEntity player;
	private int aura, auraLock, auraTimer;

	public AuraComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		aura = tag.getInt("Aura");
		auraLock = tag.getInt("AuraLock");
		auraTimer = tag.getInt("AuraTimer");

		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Aura", aura);
		tag.putInt("AuraLock", auraLock);
		tag.putInt("AuraTimer", auraTimer);
	}

	@Override
	public void serverTick() {
		EntityAttributeInstance auraRegen = player.getAttributeInstance(Arcanus.EntityAttributes.AURA_REGEN);

		if(aura < MAX_AURA)
			auraTimer++;
		else
			auraTimer = 0;

		if(addAura(1, true) && auraTimer > 0 && auraRegen != null && auraTimer % (auraRegen.getValue() * 20) == 0)
			addAura(1, false);
	}

	public int getAura() {
		return aura;
	}

	public void setAura(int amount) {
		aura = MathHelper.clamp(amount, 0, getMaxAura());
		ArcanusComponents.AURA_COMPONENT.sync(player);
	}

	public boolean addAura(int amount, boolean simulate) {
		if(getAura() < getMaxAura()) {
			if(!simulate)
				setAura(getAura() + amount);

			return true;
		}

		return false;
	}

	public boolean drainAura(int amount, boolean simulate) {
		if(getAura() - amount >= 0) {
			if(!simulate)
				setAura(getAura() - amount);

			return true;
		}

		return false;
	}

	public int getAuraLock() {
		return auraLock;
	}

	public void setAuraLock(int amount) {
		auraLock = MathHelper.clamp(amount, 0, MAX_AURA);
		setAura(getAura());
	}

	public boolean addAuraLock(int amount, boolean simulate) {
		if(getMaxAura() > 0) {
			if(!simulate)
				setAuraLock(getAuraLock() + amount);

			return true;
		}

		return false;
	}

	public int getMaxAura() {
		return MAX_AURA - getAuraLock();
	}
}

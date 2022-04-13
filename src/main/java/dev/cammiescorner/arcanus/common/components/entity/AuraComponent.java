package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class AuraComponent implements AutoSyncedComponent, ServerTickingComponent {
	private static final int MAX_AURA = 20;
	private final LivingEntity entity;
	private int aura, auraLock, auraTimer;

	public AuraComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		int a = tag.getInt("Aura");
		aura = a & 31;
		auraLock = a >> 5 & 31;
		auraTimer = tag.getInt("AuraTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Aura", (auraLock << 5) + (aura & 31));
		tag.putInt("AuraTimer", auraTimer);
	}

	@Override
	public void serverTick() {
		EntityAttributeInstance auraRegen = entity.getAttributeInstance(ArcanusAttributes.AURA_REGEN);
		EntityAttributeInstance auraLock = entity.getAttributeInstance(ArcanusAttributes.AURA_LOCK);
		this.auraLock = auraLock != null ? (int) auraLock.getValue() : 0;

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
		ArcanusComponents.AURA_COMPONENT.sync(entity);
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

	public int getMaxAura() {
		return MAX_AURA - auraLock;
	}
}

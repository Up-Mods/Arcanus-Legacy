package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class AuraComponent implements AutoSyncedComponent, ServerTickingComponent {
	public static final int MAX_AURA = 20;
	private final PlayerEntity player;
	private int aura, auraTimer;

	public AuraComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		aura = tag.getInt("Aura");
		auraTimer = tag.getInt("AuraTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("Aura", aura);
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
		aura = amount;
		ArcanusComponents.AURA_COMPONENT.sync(player);
	}

	public boolean addAura(int amount, boolean simulate) {
		if(getAura() < MAX_AURA) {
			if(!simulate)
				setAura(Math.min(MAX_AURA, getAura() + amount));

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
}

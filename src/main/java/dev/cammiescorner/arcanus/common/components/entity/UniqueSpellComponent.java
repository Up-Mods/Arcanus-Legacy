package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class UniqueSpellComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private final Spell spell;
	private boolean active;

	public UniqueSpellComponent(LivingEntity entity, Spell spell) {
		this.entity = entity;
		this.spell = spell;
	}

	@Override
	public void serverTick() {
		if(isActive())
			ArcanusHelper.castSpell(spell, entity.world, entity, entity.getPos());
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		active = tag.getBoolean("IsActive");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("IsActive", active);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;

		if(spell.equals(ArcanusSpells.TEMPORAL_DISRUPTION))
			ArcanusComponents.TEMPORAL_DISRUPTION_COMPONENT.sync(entity);
	}
}

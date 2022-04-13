package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class SpellCooldownComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity player;
	private int spellCooldown = 0;

	public SpellCooldownComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void serverTick() {
		if(getSpellCooldown() > 0)
			setSpellCooldown(getSpellCooldown() - 1);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		spellCooldown = tag.getInt("SpellCooldown");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("SpellCooldown", spellCooldown);
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public void setSpellCooldown(int value) {
		spellCooldown = value;
		ArcanusComponents.SPELL_COOLDOWN_COMPONENT.sync(player);
	}
}

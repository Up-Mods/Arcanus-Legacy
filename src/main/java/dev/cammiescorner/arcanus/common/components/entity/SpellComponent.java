package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class SpellComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private Spell spell;

	public SpellComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		spell = Arcanus.SPELL.get(new Identifier(tag.getString("SelectedSpell")));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("SelectedSpell", Arcanus.SPELL.getId(spell).toString());
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}
}

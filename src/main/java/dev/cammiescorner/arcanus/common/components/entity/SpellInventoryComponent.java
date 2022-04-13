package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class SpellInventoryComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private final DefaultedList<Spell> spells = DefaultedList.ofSize(10, ArcanusSpells.EMPTY);
	private int maxSpells = 2;

	public SpellInventoryComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList listTag = tag.getList("Spells", NbtType.STRING);

		maxSpells = tag.getInt("MaxSpells");

		for(int i = 0; i < listTag.size(); i++)
			spells.set(i, Arcanus.SPELL.get(new Identifier(listTag.getString(i))));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList listTag = new NbtList();

		tag.putInt("MaxSpells", maxSpells);
		spells.forEach(value -> listTag.add(NbtString.of(Arcanus.SPELL.getId(value).toString())));
		tag.put("Spells", listTag);
	}

	public int getMaxSpells() {
		return maxSpells;
	}

	public void setMaxSpells(int amount) {
		maxSpells = amount;
		ArcanusComponents.SPELL_INVENTORY_COMPONENT.sync(player);
	}

	public DefaultedList<Spell> getAllSpells() {
		return spells;
	}

	public Spell getSpellInSlot(int index) {
		return getAllSpells().get(index);
	}

	public void setSpellInSlot(Spell spell, int index) {
		getAllSpells().set(index, spell);
		ArcanusComponents.SPELL_INVENTORY_COMPONENT.sync(player);
	}
}

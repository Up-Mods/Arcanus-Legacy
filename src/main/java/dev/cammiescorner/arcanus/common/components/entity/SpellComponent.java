package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class SpellComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final DefaultedList<Spell> spells = DefaultedList.ofSize(8, ArcanusSpells.EMPTY);
	private final PlayerEntity player;
	private Spell activatedSpell;
	private int selectedSpell = 0;
	private int maxSpells = 2;
	private int spellCooldown = 0;

	public SpellComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void serverTick() {
		if(getSpellCooldown() > 0)
			setSpellCooldown(getSpellCooldown() - 1);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtCompound rootTag = tag.getCompound(Arcanus.MOD_ID);
		NbtList listTag = rootTag.getList("Spells", NbtType.STRING);

		maxSpells = rootTag.getInt("MaxSpells");
		selectedSpell = rootTag.getInt("SelectedSpell");
		spellCooldown = rootTag.getInt("SpellCooldown");
		activatedSpell = Arcanus.SPELL.get(new Identifier(rootTag.getString("ActivatedSpell")));

		for(int i = 0; i < listTag.size(); i++)
			spells.set(i, Arcanus.SPELL.get(new Identifier(listTag.getString(i))));

		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtCompound rootTag = new NbtCompound();
		NbtList listTag = new NbtList();

		tag.put(Arcanus.MOD_ID, rootTag);
		rootTag.putInt("MaxSpells", maxSpells);
		rootTag.putInt("SelectedSpell", selectedSpell);
		rootTag.putInt("SpellCooldown", spellCooldown);
		rootTag.putString("ActivatedSpell", Arcanus.SPELL.getId(activatedSpell).toString());
		spells.forEach(value -> listTag.add(NbtString.of(Arcanus.SPELL.getId(value).toString())));
		rootTag.put("Spells", listTag);
	}

	public Spell getSelectedSpell() {
		return getAllSpells().get(selectedSpell);
	}

	public void setSelectedSpell(int value) {
		selectedSpell = value;
		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}

	public int getMaxSpells() {
		return maxSpells;
	}

	public void setMaxSpells(int amount) {
		maxSpells = amount;
		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public void setSpellCooldown(int value) {
		spellCooldown = value;
		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}

	public Spell getActivatedSpell() {
		return activatedSpell;
	}

	public void activateSpell(Spell spell) {
		activatedSpell = spell;
		spell.cast(player.world, player);
		setSpellCooldown(spell.getSpellCooldown());
	}

	public void activateCurrentSpell() {
		activateSpell(getSelectedSpell());
	}

	public DefaultedList<Spell> getAllSpells() {
		return spells;
	}

	public Spell getSpellInSlot(int index) {
		return getAllSpells().get(index);
	}

	public void setSpellInSlot(Spell spell, int index) {
		getAllSpells().set(index, spell);
		ArcanusComponents.SPELL_COMPONENT.sync(player);
	}
}

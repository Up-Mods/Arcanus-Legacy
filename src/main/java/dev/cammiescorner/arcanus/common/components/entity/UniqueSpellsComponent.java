package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusSpells;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class UniqueSpellsComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private final Set<Spell> SPELLS = new HashSet<>();

	public UniqueSpellsComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		for(Spell spell : SPELLS)
			if(isActive(spell))
				ArcanusHelper.castSpell(spell, entity.getWorld(), entity, entity.getPos());
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList list = tag.getList("ActiveUniqueSpells", NbtElement.STRING_TYPE);

		for(int i = 0; i < list.size(); i++) {
			Spell spell = Arcanus.SPELL.get(new Identifier(list.getString(i)));

			if(!spell.equals(ArcanusSpells.EMPTY))
				SPELLS.add(spell);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();

		SPELLS.forEach(spell -> list.add(NbtString.of(Arcanus.SPELL.getId(spell).toString())));
		tag.put("ActiveUniqueSpells", list);
	}

	public boolean isActive(Spell spell) {
		return SPELLS.contains(spell);
	}

	public void setActive(Spell spell, boolean active) {
		if(active)
			SPELLS.add(spell);
		else
			SPELLS.remove(spell);

		ArcanusComponents.UNIQUE_SPELLS_COMPONENT.sync(entity);
	}
}

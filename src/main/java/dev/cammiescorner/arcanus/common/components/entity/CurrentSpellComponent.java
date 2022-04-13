package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CurrentSpellComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private Spell activatedSpell;
	private int selectedSpell = 0;

	public CurrentSpellComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		selectedSpell = tag.getInt("SelectedSpell");
		activatedSpell = Arcanus.SPELL.get(new Identifier(tag.getString("ActivatedSpell")));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("SelectedSpell", selectedSpell);
		tag.putString("ActivatedSpell", Arcanus.SPELL.getId(activatedSpell).toString());
	}

	public Spell getSelectedSpell() {
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(entity).getAllSpells().get(selectedSpell);
	}

	public void setSelectedSpell(int value) {
		selectedSpell = value;
		ArcanusComponents.CURRENT_SPELL_COMPONENT.sync(entity);
	}

	public Spell getCastSpell() {
		return activatedSpell;
	}

	public void castSpell(Spell spell, World world, LivingEntity entity, Vec3d pos) {
		activatedSpell = spell;
		spell.cast(world, entity, pos);
		ArcanusHelper.setSpellCooldown(entity, spell.getSpellCooldown());

		if(entity instanceof PlayerEntity player && !player.isCreative())
			ArcanusHelper.drainAura(player, ArcanusHelper.actualAuraCost(player, spell), false);
	}

	public void castCurrentSpell() {
		castSpell(getSelectedSpell(), entity.world, entity, entity.getPos());
	}
}

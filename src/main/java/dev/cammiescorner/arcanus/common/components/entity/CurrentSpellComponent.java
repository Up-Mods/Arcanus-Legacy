package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CurrentSpellComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private Spell activatedSpell;
	private int selectedSpell = 0;

	public CurrentSpellComponent(PlayerEntity player) {
		this.player = player;
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
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(player).getAllSpells().get(selectedSpell);
	}

	public void setSelectedSpell(int value) {
		selectedSpell = value;
		ArcanusComponents.CURRENT_SPELL_COMPONENT.sync(player);
	}

	public Spell getCastSpell() {
		return activatedSpell;
	}

	public void castSpell(Spell spell, World world, PlayerEntity player, Vec3d pos) {
		activatedSpell = spell;
		spell.cast(world, player, pos);
		ArcanusHelper.setSpellCooldown(player, spell.getSpellCooldown());

		if(!player.isCreative())
			ArcanusHelper.drainAura(player, ArcanusHelper.actualAuraCost(player, spell), false);
	}

	public void castCurrentSpell() {
		castSpell(getSelectedSpell(), player.world, player, player.getPos());
	}
}

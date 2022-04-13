package dev.cammiescorner.arcanus.api;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.cults.Cults;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ArcanusHelper {
	public static int getAura(PlayerEntity player) {
		return ArcanusComponents.AURA_COMPONENT.get(player).getAura();
	}

	public static void setAura(PlayerEntity player, int amount) {
		ArcanusComponents.AURA_COMPONENT.get(player).setAura(amount);
	}

	public static boolean addAura(PlayerEntity player, int amount, boolean simulate) {
		return ArcanusComponents.AURA_COMPONENT.get(player).addAura(amount, simulate);
	}

	public static boolean drainAura(PlayerEntity player, int amount, boolean simulate) {
		return ArcanusComponents.AURA_COMPONENT.get(player).drainAura(amount, simulate);
	}

	public static int getMaxAura(PlayerEntity player) {
		return ArcanusComponents.AURA_COMPONENT.get(player).getMaxAura();
	}

	public static boolean isCasting(PlayerEntity player) {
		return ArcanusComponents.CASTING_COMPONENT.get(player).isCasting();
	}

	public static void setCasting(PlayerEntity player, boolean casting) {
		ArcanusComponents.CASTING_COMPONENT.get(player).setCasting(casting);
	}

	public static int getMaxSpells(PlayerEntity player) {
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(player).getMaxSpells();
	}

	public static void setMaxSpells(PlayerEntity player, int amount) {
		ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(player).setMaxSpells(amount);
	}

	public static DefaultedList<Spell> getAllSpells(PlayerEntity player) {
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(player).getAllSpells();
	}

	public static Spell getSpellInSlot(PlayerEntity player, int index) {
		return getAllSpells(player).get(index);
	}

	public static void setSpellInSlot(PlayerEntity player, Spell spell, int index) {
		getAllSpells(player).set(index, spell);
	}

	public static Spell getSelectedSpell(PlayerEntity player) {
		return ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player).getSelectedSpell();
	}

	public static void setSelectedSpell(PlayerEntity player, int value) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player).setSelectedSpell(value);
	}

	public static Spell getCastSpell(PlayerEntity player) {
		return ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player).getCastSpell();
	}

	public static void castSpell(Spell spell, World world, PlayerEntity player, Vec3d pos) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player).castSpell(spell, world, player, pos);
	}

	public static void castSelectedSpell(PlayerEntity player) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player).castCurrentSpell();
	}

	public static int getSpellCooldown(PlayerEntity player) {
		return ArcanusComponents.SPELL_COOLDOWN_COMPONENT.get(player).getSpellCooldown();
	}

	public static void setSpellCooldown(PlayerEntity player, int value) {
		ArcanusComponents.SPELL_COOLDOWN_COMPONENT.get(player).setSpellCooldown(value);
	}

	public static boolean canCastSpell(PlayerEntity player, Spell spell) {
		return isCasting(player) && getSpellCooldown(player) <= 0 && drainAura(player, actualAuraCost(player, spell), true) && spell.getSpellType() != Spell.Type.NONE;
	}

	public static int actualAuraCost(PlayerEntity player, Spell spell) {
		EntityAttributeInstance instance = player.getAttributeInstance(Arcanus.EntityAttributes.AURA_COST);

		if(instance != null)
			return (int) (spell.getAuraCost() * instance.getValue());

		return spell.getAuraCost();
	}

	public static Cults getCult(PlayerEntity player) {
		return ArcanusComponents.CULT_COMPONENT.get(player).getCult();
	}

	public static void setCult(PlayerEntity player, Cults cult) {
		ArcanusComponents.CULT_COMPONENT.get(player).setCult(cult);
	}

	public static int getReputation(PlayerEntity player) {
		return ArcanusComponents.CULT_COMPONENT.get(player).getReputation();
	}

	public static void setReputation(PlayerEntity player, int amount) {
		ArcanusComponents.CULT_COMPONENT.get(player).setReputation(amount);
	}

	public static boolean addReputation(PlayerEntity player, int amount, boolean simulate) {
		return ArcanusComponents.CULT_COMPONENT.get(player).addReputation(amount, simulate);
	}

	public static boolean reduceReputation(PlayerEntity player, int amount, boolean simulate) {
		return ArcanusComponents.CULT_COMPONENT.get(player).reduceReputation(amount, simulate);
	}
}

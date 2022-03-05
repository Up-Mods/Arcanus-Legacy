package dev.cammiescorner.arcanus.api.spells;

import dev.cammiescorner.arcanus.common.components.entity.SpellComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class Spell {
	private Type spellType;
	private int spellCooldown;

	public Spell(Type type, int cooldown) {
		spellType = type;
		spellCooldown = cooldown;
	}

	public Type getSpellType() {
		return spellType;
	}

	public void setSpellType(Type type) {
		spellType = type;
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public void setSpellCooldown(int value) {
		spellCooldown = value;
	}

	public boolean isActive(PlayerEntity player) {
		SpellComponent spellComponent = ArcanusComponents.SPELL_COMPONENT.get(player);

		return spellComponent.getActivatedSpell().equals(this);
	}

	/**
	 * Super at the beginning of every override.
	 * @param world it was me, DIO!
	 * @param player some weirdo probably.
	 */
	public void cast(World world, PlayerEntity player) {
		if(isActive(player))
			return;
	}

	public enum Type {
		ENHANCER(0x008100), TRANSMUTER(0xbb00c5), EMITTER(0xe7bd00), CONJURER(0xda0018),
		MANIPULATOR(0xc1c1c1), SPECIALIST(0x0070b9), NONE(0xFFFFFF);

		private final int colour;

		Type(int colour) {
			this.colour = colour;
		}

		public int getDecimal() {
			return colour;
		}

		public float[] getRgb() {
			return new float[] {
				(colour >> 16 & 255) / 255F,
				(colour >> 8 & 255) / 255F,
				(colour & 255) / 255F
			};
		}
	}
}

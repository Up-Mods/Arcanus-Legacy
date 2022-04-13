package dev.cammiescorner.arcanus.api.spells;

import dev.cammiescorner.arcanus.common.components.entity.CurrentSpellComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Spell {
	private final Type spellType;
	private final Complexity spellComplexity;
	private final int spellCooldown;

	public Spell(Type type, Complexity complexity, int cooldown) {
		spellType = type;
		spellComplexity = complexity;
		spellCooldown = cooldown;
	}

	public Type getSpellType() {
		return spellType;
	}

	public Complexity getSpellComplexity() {
		return spellComplexity;
	}

	public int getAuraCost() {
		return spellComplexity.getAuraCost();
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public boolean isActive(PlayerEntity player) {
		CurrentSpellComponent spellComponent = ArcanusComponents.CURRENT_SPELL_COMPONENT.get(player);

		return spellComponent.getCastSpell().equals(this);
	}

	public void cast(World world, PlayerEntity player, Vec3d pos) {

	}

	public enum Complexity {
		SIMPLE(1), INTRICATE(2), COMPLEX(3), UNIQUE(0);

		private final int auraCost;

		Complexity(int auraCost) {
			this.auraCost = auraCost;
		}

		public int getAuraCost() {
			return auraCost;
		}
	}

	public enum Type {
		ENHANCER(0x008100), TRANSMUTER(0xbb00c5), EMITTER(0xffd100),
		CONJURER(0xda0018), MANIPULATOR(0xe08600), SPECIALIST(0x0070b9),
		NONE(0xffffff);

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

		public int[] getRgbInt() {
			return new int[] {
					(colour >> 16 & 255),
					(colour >> 8 & 255),
					(colour & 255)
			};
		}
	}
}

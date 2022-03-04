package dev.cammiescorner.arcanus.api.spells;

public class Spell {
	private Type spellType = Type.NONE;

	public Spell(Type type) {
		spellType = type;
	}

	public Type getSpellType() {
		return spellType;
	}

	public void setSpellType(Type type) {
		spellType = type;
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

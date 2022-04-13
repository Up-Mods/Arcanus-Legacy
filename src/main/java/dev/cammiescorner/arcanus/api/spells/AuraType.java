package dev.cammiescorner.arcanus.api.spells;

public enum AuraType {
	ENHANCER(0x008100), TRANSMUTER(0xbb00c5), EMITTER(0xffd100),
	CONJURER(0xda0018), MANIPULATOR(0xe08600), SPECIALIST(0x0070b9),
	NONE(0xffffff);

	private final int colour;

	AuraType(int colour) {
		this.colour = colour;
	}

	public int getDecimal() {
		return colour;
	}

	public float[] getRgb() {
		return new float[]{(colour >> 16 & 255) / 255F, (colour >> 8 & 255) / 255F, (colour & 255) / 255F};
	}

	public int[] getRgbInt() {
		return new int[]{(colour >> 16 & 255), (colour >> 8 & 255), (colour & 255)};
	}
}

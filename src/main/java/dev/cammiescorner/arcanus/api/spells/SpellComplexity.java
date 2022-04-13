package dev.cammiescorner.arcanus.api.spells;

public enum SpellComplexity {
	SIMPLE(1), INTRICATE(2), COMPLEX(3), UNIQUE(0);

	private final int auraCost;

	SpellComplexity(int auraCost) {
		this.auraCost = auraCost;
	}

	public int getAuraCost() {
		return auraCost;
	}
}

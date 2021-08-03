package dev.cammiescorner.arcanus.core.util;

public enum Pattern {
	LEFT("L"), RIGHT("R"), ALT_LEFT("AL"), ALT_RIGHT("AR");

	String symbol;

	Pattern(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}

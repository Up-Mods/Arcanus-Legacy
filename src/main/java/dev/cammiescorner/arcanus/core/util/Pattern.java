package dev.cammiescorner.arcanus.core.util;

public enum Pattern {
    LEFT("L"), RIGHT("R");

    String symbol;

    Pattern(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

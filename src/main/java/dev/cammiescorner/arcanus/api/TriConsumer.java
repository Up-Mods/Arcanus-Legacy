package dev.cammiescorner.arcanus.api;

public interface TriConsumer<A, B, C> {
	void accept(A a, B b, C c);
}

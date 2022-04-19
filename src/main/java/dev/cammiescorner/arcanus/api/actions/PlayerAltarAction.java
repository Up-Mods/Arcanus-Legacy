package dev.cammiescorner.arcanus.api.actions;

public interface PlayerAltarAction extends AltarAction {
	@Override
	default boolean requiresPlayer() {
		return true;
	}
}

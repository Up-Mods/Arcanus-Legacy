package dev.cammiescorner.arcanus.common.actions;

import dev.cammiescorner.arcanus.api.recipes.AltarAction;

public interface PlayerAltarAction extends AltarAction {
	@Override
	default boolean requiresPlayer() {
		return true;
	}
}

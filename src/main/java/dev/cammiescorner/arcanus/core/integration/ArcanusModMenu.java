package dev.cammiescorner.arcanus.core.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.arcanus.Arcanus;

public class ArcanusModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> ArcanusConfig.getScreen(parent, Arcanus.MOD_ID);
	}
}

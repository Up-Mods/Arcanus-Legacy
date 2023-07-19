package dev.cammiescorner.arcanus.client.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.arcanus.core.integration.ArcanusConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class ArcanusModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(ArcanusConfig.class, parent).get();
	}
}

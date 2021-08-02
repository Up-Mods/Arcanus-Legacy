package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class ModEntityLayers {
	//-----Model Layer Map-----//
	public static final LinkedHashMap<EntityModelLayer, Identifier> LAYERS = new LinkedHashMap<>();

	//-----Registry-----//
	public static void register() {

	}

	private static EntityModelLayer create(String name) {
		EntityModelLayer layer = new EntityModelLayer(new Identifier(Arcanus.MOD_ID, name), name);
		LAYERS.put(layer, new Identifier(Arcanus.MOD_ID, name));
		return layer;
	}
}

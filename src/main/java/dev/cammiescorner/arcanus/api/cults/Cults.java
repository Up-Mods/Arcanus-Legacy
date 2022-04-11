package dev.cammiescorner.arcanus.api.cults;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum Cults {
	NONE("minecraft:air"),
	NORTH("arcanus:diamond_gemstone_tablet"),
	EAST("arcanus:amethyst_gemstone_tablet"),
	SOUTH("arcanus:quartz_gemstone_tablet"),
	WEST("arcanus:emerald_gemstone_tablet");

	private final Identifier tabletId;

	Cults(String tabletId) {
		this.tabletId = new Identifier(tabletId);
	}

	public Item getGemstoneTablet() {
		return Registry.ITEM.get(tabletId);
	}
}

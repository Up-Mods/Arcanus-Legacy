package dev.cammiescorner.arcanus.common.items;

import dev.cammiescorner.arcanus.common.registry.ArcanusMaterials;
import net.minecraft.entity.EquipmentSlot;

public class TimeCultistRobesItem extends RobesItem {
	private final boolean isLeaderRobes;

	public TimeCultistRobesItem(EquipmentSlot slot, boolean isLeaderRobes) {
		super(isLeaderRobes ? ArcanusMaterials.Armour.LEADER_ROBES : ArcanusMaterials.Armour.MAGE_ROBES, slot);
		this.isLeaderRobes = isLeaderRobes;
	}

	public boolean isLeaderRobes() {
		return isLeaderRobes;
	}
}

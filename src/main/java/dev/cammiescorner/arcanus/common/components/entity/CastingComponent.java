package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class CastingComponent implements AutoSyncedComponent {
	private final PlayerEntity player;
	private boolean isCasting;

	public CastingComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		isCasting = tag.getBoolean("IsCasting");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("IsCasting", isCasting);
	}

	public boolean isCasting() {
		return isCasting;
	}

	public void setCasting(boolean casting) {
		isCasting = casting;
		ArcanusComponents.CASTING_COMPONENT.sync(player);
	}
}

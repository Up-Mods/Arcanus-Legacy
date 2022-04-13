package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class CastingComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private boolean isCasting;

	public CastingComponent(LivingEntity entity) {
		this.entity = entity;
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
		ArcanusComponents.CASTING_COMPONENT.sync(entity);
	}
}

package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.api.cults.Cults;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class CultComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	private Cults cult = Cults.NONE;
	private int reputation;

	public CultComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		int a = tag.getInt("CultReputation");

		cult = Cults.values()[a & 7];
		reputation = a >> 3 & 15;
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("CultReputation", (reputation << 3) + (cult.ordinal() & 7));
	}

	public Cults getCult() {
		return cult;
	}

	public void setCult(Cults cult) {
		this.cult = cult;
		ArcanusComponents.CULT_COMPONENT.sync(entity);
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int amount) {
		reputation = MathHelper.clamp(amount, 0, 10);
		ArcanusComponents.CULT_COMPONENT.sync(entity);
	}

	public boolean addReputation(int amount, boolean simulate) {
		if(getReputation() < 10) {
			if(!simulate)
				setReputation(getReputation() + amount);

			return true;
		}

		return false;
	}

	public boolean reduceReputation(int amount, boolean simulate) {
		if(getReputation() - amount >= 0) {
			if(!simulate)
				setReputation(getReputation() - amount);

			return true;
		}

		return false;
	}
}

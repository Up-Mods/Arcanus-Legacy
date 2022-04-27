package dev.cammiescorner.arcanus.common.components.entity;

import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class AuraAffinityComponent implements AutoSyncedComponent {
	private final LivingEntity entity;
	public static final UUID ENHANCEMENT_AFFINITY_UUID = UUID.fromString("2c02e5b8-b75e-44a1-b427-aacbad48e1f8");
	public static final UUID TRANSMUTATION_AFFINITY_UUID = UUID.fromString("7da12750-1910-4a5c-99a1-98018cb64da1");
	public static final UUID EMISSION_AFFINITY_UUID = UUID.fromString("e30be356-8194-41ad-bd1a-21b63ddaf906");
	public static final UUID CONJURATION_AFFINITY_UUID = UUID.fromString("c048db0a-29f6-42c1-9cbc-848380e1f02e");
	public static final UUID MANIPULATION_AFFINITY_UUID = UUID.fromString("c048db0a-29f6-42c1-9cbc-848380e1f02e");
	private AuraType affinity = AuraType.NONE;

	public AuraAffinityComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		affinity = AuraType.values()[tag.getInt("AuraAffinity")];
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("AuraAffinity", affinity.ordinal());
	}

	public AuraType getAffinity() {
		return affinity;
	}

	public void setAffinity(AuraType affinity) {
		this.affinity = affinity;
		double enhancer, transmuter, emitter, conjurer, manipulator;
		EntityAttributeInstance enhancement = entity.getAttributeInstance(ArcanusAttributes.ENHANCEMENT_AFFINITY);
		EntityAttributeInstance transmutation = entity.getAttributeInstance(ArcanusAttributes.TRANSMUTATION_AFFINITY);
		EntityAttributeInstance emission = entity.getAttributeInstance(ArcanusAttributes.EMISSION_AFFINITY);
		EntityAttributeInstance conjuration = entity.getAttributeInstance(ArcanusAttributes.CONJURATION_AFFINITY);
		EntityAttributeInstance manipulation = entity.getAttributeInstance(ArcanusAttributes.MANIPULATION_AFFINITY);

		if(enhancement != null)
			enhancement.tryRemoveModifier(ENHANCEMENT_AFFINITY_UUID);
		if(transmutation != null)
			transmutation.tryRemoveModifier(TRANSMUTATION_AFFINITY_UUID);
		if(emission != null)
			emission.tryRemoveModifier(EMISSION_AFFINITY_UUID);
		if(conjuration != null)
			conjuration.tryRemoveModifier(CONJURATION_AFFINITY_UUID);
		if(manipulation != null)
			manipulation.tryRemoveModifier(MANIPULATION_AFFINITY_UUID);

		switch(affinity) {
			case ENHANCER -> {
				enhancer = 0.5;
				transmuter = 0.3D;
				emitter = 0.3D;
				conjurer = -0.1D;
				manipulator = -0.1D;
			}
			case TRANSMUTER -> {
				enhancer = 0.3D;
				transmuter = 0.5D;
				emitter = -0.1D;
				conjurer = 0.3D;
				manipulator = 0.1D;
			}
			case EMITTER -> {
				enhancer = 0.3D;
				transmuter = -0.1D;
				emitter = 0.5D;
				conjurer = -0.1D;
				manipulator = 0.3D;
			}
			case CONJURER -> {
				enhancer = -0.1D;
				transmuter = 0.3D;
				emitter = -0.1D;
				conjurer = 0.5D;
				manipulator = 0.3D;
			}
			case MANIPULATOR -> {
				enhancer = -0.1D;
				transmuter = -0.1D;
				emitter = 0.3D;
				conjurer = 0.3D;
				manipulator = 0.5D;
			}
			default -> {
				enhancer = 0D;
				transmuter = 0D;
				emitter = 0D;
				conjurer = 0D;
				manipulator = 0D;
			}
		}

		if(!(affinity.equals(AuraType.NONE) || affinity.equals(AuraType.SPECIALIST))) {
			EntityAttributeModifier enhancementMod = new EntityAttributeModifier(ENHANCEMENT_AFFINITY_UUID, "Enhancement Affinity", enhancer, EntityAttributeModifier.Operation.ADDITION);
			EntityAttributeModifier transmutationMod = new EntityAttributeModifier(TRANSMUTATION_AFFINITY_UUID, "Transmutation Affinity", transmuter, EntityAttributeModifier.Operation.ADDITION);
			EntityAttributeModifier emissionMod = new EntityAttributeModifier(EMISSION_AFFINITY_UUID, "Emission Affinity", emitter, EntityAttributeModifier.Operation.ADDITION);
			EntityAttributeModifier conjurationMod = new EntityAttributeModifier(CONJURATION_AFFINITY_UUID, "Conjuration Affinity", conjurer, EntityAttributeModifier.Operation.ADDITION);
			EntityAttributeModifier manipulationMod = new EntityAttributeModifier(MANIPULATION_AFFINITY_UUID, "Manipulation Affinity", manipulator, EntityAttributeModifier.Operation.ADDITION);

			if(enhancement != null)
				enhancement.addPersistentModifier(enhancementMod);
			if(transmutation != null)
				transmutation.addPersistentModifier(transmutationMod);
			if(emission != null)
				emission.addPersistentModifier(emissionMod);
			if(conjuration != null)
				conjuration.addPersistentModifier(conjurationMod);
			if(manipulation != null)
				manipulation.addPersistentModifier(manipulationMod);
		}

		ArcanusComponents.AURA_AFFINITY_COMPONENT.sync(entity);
	}
}

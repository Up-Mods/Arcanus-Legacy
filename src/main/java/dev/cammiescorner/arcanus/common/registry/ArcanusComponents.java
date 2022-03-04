package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.components.entity.AuraComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusComponents implements EntityComponentInitializer {
	public static final ComponentKey<AuraComponent> AURA_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("aura"), AuraComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, AURA_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(AuraComponent::new);
	}
}

package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.components.entity.AuraComponent;
import dev.cammiescorner.arcanus.common.components.entity.CultComponent;
import dev.cammiescorner.arcanus.common.components.entity.SpellComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusComponents implements EntityComponentInitializer {
	public static final ComponentKey<AuraComponent> AURA_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("aura"), AuraComponent.class);
	public static final ComponentKey<SpellComponent> SPELL_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("spell"), SpellComponent.class);
	public static final ComponentKey<CultComponent> CULT_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("cult"), CultComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, AURA_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(AuraComponent::new);
		registry.beginRegistration(PlayerEntity.class, SPELL_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(SpellComponent::new);
		registry.beginRegistration(LivingEntity.class, CULT_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CultComponent::new);
	}
}

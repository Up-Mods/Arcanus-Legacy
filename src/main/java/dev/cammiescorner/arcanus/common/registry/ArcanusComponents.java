package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.components.entity.*;
import dev.cammiescorner.arcanus.common.components.world.AltarStructureComponent;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusComponents implements EntityComponentInitializer, WorldComponentInitializer, ChunkComponentInitializer {
	public static final ComponentKey<AuraComponent> AURA_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("aura"), AuraComponent.class);
	public static final ComponentKey<CastingComponent> CASTING_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("casting"), CastingComponent.class);
	public static final ComponentKey<CultComponent> CULT_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("cult"), CultComponent.class);
	public static final ComponentKey<CurrentSpellComponent> CURRENT_SPELL_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("current_spell"), CurrentSpellComponent.class);
	public static final ComponentKey<SpellInventoryComponent> SPELL_INVENTORY_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("spell_inventory"), SpellInventoryComponent.class);
	public static final ComponentKey<SpellCooldownComponent> SPELL_COOLDOWN_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("spell_cooldown"), SpellCooldownComponent.class);
	public static final ComponentKey<AuraFadeComponent> AURA_FADE_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("aura_fade"), AuraFadeComponent.class);

	public static final ComponentKey<AltarStructureComponent> ALTAR_STRUCTURE_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("altar_structure"), AltarStructureComponent.class);

	public static final ComponentKey<PurpleWaterComponent> PURPLE_WATER_COMPONENT = ComponentRegistry.getOrCreate(Arcanus.id("purple_water"), PurpleWaterComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, AURA_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(AuraComponent::new);
		registry.beginRegistration(PlayerEntity.class, CASTING_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(CastingComponent::new);
		registry.beginRegistration(PlayerEntity.class, CULT_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CultComponent::new);
		registry.beginRegistration(PlayerEntity.class, CURRENT_SPELL_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CurrentSpellComponent::new);
		registry.beginRegistration(PlayerEntity.class, SPELL_INVENTORY_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(SpellInventoryComponent::new);
		registry.beginRegistration(PlayerEntity.class, SPELL_COOLDOWN_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpellCooldownComponent::new);
		registry.beginRegistration(PlayerEntity.class, AURA_FADE_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AuraFadeComponent::new);
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(ALTAR_STRUCTURE_COMPONENT, AltarStructureComponent::new);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(PURPLE_WATER_COMPONENT, PurpleWaterComponent::new);
	}
}

package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.components.chunk.PurpleWaterComponent;
import dev.cammiescorner.arcanus.common.components.entity.*;
import dev.cammiescorner.arcanus.common.components.world.AltarStructureComponent;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusComponents implements EntityComponentInitializer, WorldComponentInitializer, ChunkComponentInitializer {
	public static final ComponentKey<AuraComponent> AURA_COMPONENT = createComponent("aura", AuraComponent.class);
	public static final ComponentKey<CastingComponent> CASTING_COMPONENT = createComponent("casting", CastingComponent.class);
	public static final ComponentKey<CultComponent> CULT_COMPONENT = createComponent("cult", CultComponent.class);
	public static final ComponentKey<CurrentSpellComponent> CURRENT_SPELL_COMPONENT = createComponent("current_spell", CurrentSpellComponent.class);
	public static final ComponentKey<SpellInventoryComponent> SPELL_INVENTORY_COMPONENT = createComponent("spell_inventory", SpellInventoryComponent.class);
	public static final ComponentKey<SpellCooldownComponent> SPELL_COOLDOWN_COMPONENT = createComponent("spell_cooldown", SpellCooldownComponent.class);
	public static final ComponentKey<AuraFadeComponent> AURA_FADE_COMPONENT = createComponent("aura_fade", AuraFadeComponent.class);
	public static final ComponentKey<AuraAffinityComponent> AURA_AFFINITY_COMPONENT = createComponent("aura_affinity", AuraAffinityComponent.class);

	public static final ComponentKey<UniqueSpellComponent> TEMPORAL_DISRUPTION_COMPONENT = createComponent("temporal_disruption", UniqueSpellComponent.class);
	public static final ComponentKey<UniqueSpellComponent> PLANESWALKER_COMPONENT = createComponent("planeswalker", UniqueSpellComponent.class);
	public static final ComponentKey<UniqueSpellComponent> RESTRAINT_LEVEL_ZERO_COMPONENT = createComponent("restraint_level_zero", UniqueSpellComponent.class);
	public static final ComponentKey<UniqueSpellComponent> AURA_PRODIGY_COMPONENT = createComponent("aura_prodigy_component", UniqueSpellComponent.class);

	public static final ComponentKey<AltarStructureComponent> ALTAR_STRUCTURE_COMPONENT = createComponent("altar_structure", AltarStructureComponent.class);
	public static final ComponentKey<PurpleWaterComponent> PURPLE_WATER_COMPONENT = createComponent("purple_water", PurpleWaterComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, AURA_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(AuraComponent::new);
		registry.beginRegistration(PlayerEntity.class, CASTING_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(CastingComponent::new);
		registry.beginRegistration(PlayerEntity.class, CULT_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CultComponent::new);
		registry.beginRegistration(PlayerEntity.class, CURRENT_SPELL_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(CurrentSpellComponent::new);
		registry.beginRegistration(PlayerEntity.class, SPELL_INVENTORY_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(SpellInventoryComponent::new);
		registry.beginRegistration(PlayerEntity.class, SPELL_COOLDOWN_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(SpellCooldownComponent::new);
		registry.beginRegistration(PlayerEntity.class, AURA_FADE_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(AuraFadeComponent::new);
		registry.beginRegistration(PlayerEntity.class, AURA_AFFINITY_COMPONENT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(AuraAffinityComponent::new);
		registry.beginRegistration(PlayerEntity.class, TEMPORAL_DISRUPTION_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(player -> new UniqueSpellComponent(player, ArcanusSpells.TEMPORAL_DISRUPTION));
	}

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(ALTAR_STRUCTURE_COMPONENT, AltarStructureComponent::new);
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(PURPLE_WATER_COMPONENT, PurpleWaterComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Arcanus.id(name), component);
	}
}

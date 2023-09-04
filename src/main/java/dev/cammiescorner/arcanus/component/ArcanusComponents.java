package dev.cammiescorner.arcanus.component;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.CanBeDiscombobulated;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.component.base.SpellMemory;
import dev.cammiescorner.arcanus.component.entity.LivingDiscombobulatable;
import dev.cammiescorner.arcanus.component.entity.PlayerMagicCaster;
import dev.cammiescorner.arcanus.component.entity.PlayerSpellMemory;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ArcanusComponents implements EntityComponentInitializer {

    public static final ComponentKey<MagicCaster> MAGIC_CASTER = ComponentRegistry.getOrCreate(Arcanus.id("magic_caster"), MagicCaster.class);
    public static final ComponentKey<SpellMemory> SPELL_MEMORY = ComponentRegistry.getOrCreate(Arcanus.id("spell_memory"), SpellMemory.class);
    public static final ComponentKey<CanBeDiscombobulated> CAN_BE_DISCOMBOBULATED = ComponentRegistry.getOrCreate(Arcanus.id("can_be_discombobulated"), CanBeDiscombobulated.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, MAGIC_CASTER)
                .impl(PlayerMagicCaster.class)
                .respawnStrategy(RespawnCopyStrategy.CHARACTER)
                .end(PlayerMagicCaster::new);

        registry.beginRegistration(PlayerEntity.class, SPELL_MEMORY)
                .impl(PlayerSpellMemory.class)
                .respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY)
                .end(PlayerSpellMemory::new);

        registry.beginRegistration(LivingEntity.class, CAN_BE_DISCOMBOBULATED)
                .impl(LivingDiscombobulatable.class)
                .end(LivingDiscombobulatable::new);
    }
}

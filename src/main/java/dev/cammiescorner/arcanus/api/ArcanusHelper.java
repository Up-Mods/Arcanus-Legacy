package dev.cammiescorner.arcanus.api;

import dev.cammiescorner.arcanus.api.cults.Cults;
import dev.cammiescorner.arcanus.api.entity.ArcanusAttributes;
import dev.cammiescorner.arcanus.api.spells.AuraType;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.components.entity.UniqueSpellComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import dev.cammiescorner.arcanus.common.registry.ArcanusTags;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ArcanusHelper {
	public static int getAura(Entity entity) {
		return ArcanusComponents.AURA_COMPONENT.get(entity).getAura();
	}

	public static void setAura(Entity entity, int amount) {
		ArcanusComponents.AURA_COMPONENT.get(entity).setAura(amount);
	}

	public static boolean addAura(Entity entity, int amount, boolean simulate) {
		return ArcanusComponents.AURA_COMPONENT.get(entity).addAura(amount, simulate);
	}

	public static boolean drainAura(Entity entity, int amount, boolean simulate) {
		return ArcanusComponents.AURA_COMPONENT.get(entity).drainAura(amount, simulate);
	}

	public static int getMaxAura(Entity entity) {
		return ArcanusComponents.AURA_COMPONENT.get(entity).getMaxAura();
	}

	public static boolean isCasting(Entity entity) {
		return ArcanusComponents.CASTING_COMPONENT.get(entity).isCasting();
	}

	public static void setCasting(Entity entity, boolean casting) {
		ArcanusComponents.CASTING_COMPONENT.get(entity).setCasting(casting);
	}

	public static int getMaxSpells(Entity entity) {
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(entity).getMaxSpells();
	}

	public static void setMaxSpells(Entity entity, int amount) {
		ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(entity).setMaxSpells(amount);
	}

	public static DefaultedList<Spell> getAllSpells(Entity entity) {
		return ArcanusComponents.SPELL_INVENTORY_COMPONENT.get(entity).getAllSpells();
	}

	public static Spell getSpellInSlot(Entity entity, int index) {
		return getAllSpells(entity).get(index);
	}

	public static void setSpellInSlot(Entity entity, Spell spell, int index) {
		getAllSpells(entity).set(index, spell);
	}

	public static Spell getSelectedSpell(Entity entity) {
		return ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity).getSelectedSpell();
	}

	public static void setSelectedSpell(Entity entity, int value) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity).setSelectedSpell(value);
	}

	public static void castSpell(Spell spell, World world, LivingEntity entity, Vec3d pos) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity).castSpell(spell, world, entity, pos);
	}

	public static void castCurrentSpell(Entity entity) {
		ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity).castCurrentSpell();
	}

	public static int getSpellCooldown(Entity entity) {
		return ArcanusComponents.SPELL_COOLDOWN_COMPONENT.get(entity).getSpellCooldown();
	}

	public static void setSpellCooldown(Entity entity, int value) {
		ArcanusComponents.SPELL_COOLDOWN_COMPONENT.get(entity).setSpellCooldown(value);
	}

	public static boolean canCastSpell(PlayerEntity player, Spell spell) {
		return isCasting(player) && getSpellCooldown(player) <= 0 && drainAura(player, actualAuraCost(player, spell), true) && spell.getSpellType() != AuraType.NONE;
	}

	public static int actualAuraCost(PlayerEntity player, Spell spell) {
		EntityAttributeInstance instance = player.getAttributeInstance(ArcanusAttributes.AURA_COST);

		if(instance != null)
			return (int) (spell.getAuraCost() * instance.getValue());

		return spell.getAuraCost();
	}

	public static boolean isUniqueSpellActive(Entity entity, ComponentKey<UniqueSpellComponent> componentKey) {
		return componentKey.get(entity).isActive();
	}

	public static void setUniqueSpellActive(Entity entity, ComponentKey<UniqueSpellComponent> componentKey, boolean active) {
		componentKey.get(entity).setActive(active);
	}

	public static Cults getCult(Entity entity) {
		return ArcanusComponents.CULT_COMPONENT.get(entity).getCult();
	}

	public static void setCult(Entity entity, Cults cult) {
		ArcanusComponents.CULT_COMPONENT.get(entity).setCult(cult);
	}

	public static int getReputation(Entity entity) {
		return ArcanusComponents.CULT_COMPONENT.get(entity).getReputation();
	}

	public static void setReputation(Entity entity, int amount) {
		ArcanusComponents.CULT_COMPONENT.get(entity).setReputation(amount);
	}

	public static boolean addReputation(Entity entity, int amount, boolean simulate) {
		return ArcanusComponents.CULT_COMPONENT.get(entity).addReputation(amount, simulate);
	}

	public static boolean reduceReputation(Entity entity, int amount, boolean simulate) {
		return ArcanusComponents.CULT_COMPONENT.get(entity).reduceReputation(amount, simulate);
	}

	public static float getAuraFade(Entity entity) {
		return ArcanusComponents.AURA_FADE_COMPONENT.get(entity).getTimer() * 0.1F;
	}

	public static double getAffinityMultiplier(LivingEntity entity, AuraType type) {
		return switch(type) {
			case ENHANCER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(ArcanusAttributes.ENHANCEMENT_AFFINITY);
				yield attribute != null ? attribute.getValue() : ArcanusAttributes.ENHANCEMENT_AFFINITY.getDefaultValue();
			}
			case TRANSMUTER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(ArcanusAttributes.TRANSMUTATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : ArcanusAttributes.TRANSMUTATION_AFFINITY.getDefaultValue();
			}
			case EMITTER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(ArcanusAttributes.EMISSION_AFFINITY);
				yield attribute != null ? attribute.getValue() : ArcanusAttributes.EMISSION_AFFINITY.getDefaultValue();
			}
			case CONJURER -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(ArcanusAttributes.CONJURATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : ArcanusAttributes.CONJURATION_AFFINITY.getDefaultValue();
			}
			case MANIPULATOR -> {
				EntityAttributeInstance attribute = entity.getAttributeInstance(ArcanusAttributes.MANIPULATION_AFFINITY);
				yield attribute != null ? attribute.getValue() : ArcanusAttributes.MANIPULATION_AFFINITY.getDefaultValue();
			}
			default -> 1D;
		};
	}

	public static Map<AuraType, Double> getAffinityMultipliers(LivingEntity entity) {
		HashMap<AuraType, Double> map = new HashMap<>();

		for(int i = 0; i < 7; i++) {
			AuraType type = AuraType.values()[i];
			map.put(type, getAffinityMultiplier(entity, type));
		}

		return map;
	}

	public static AuraType setAffinity(LivingEntity entity) {
		return ArcanusComponents.AURA_AFFINITY_COMPONENT.get(entity).getAffinity();
	}

	public static void setAffinity(LivingEntity entity, AuraType affinity) {
		ArcanusComponents.AURA_AFFINITY_COMPONENT.get(entity).setAffinity(affinity);
	}

	public static boolean isValidAltarBlock(BlockState state) {
		return state.isIn(ArcanusTags.ALTAR_PALETTE);
	}

	public static HashMap<BlockPos, BlockState> getStructureMap(World world) {
		return ArcanusComponents.ALTAR_STRUCTURE_COMPONENT.get(world).getStructureMap();
	}

	public static void constructStructureMap(World world) {
		ArcanusComponents.ALTAR_STRUCTURE_COMPONENT.get(world).constructStructureMap();
	}

	public static BlockPos getAltarOffset(World world) {
		return ArcanusComponents.ALTAR_STRUCTURE_COMPONENT.get(world).getOffset();
	}
}

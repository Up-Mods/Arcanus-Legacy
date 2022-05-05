package dev.cammiescorner.arcanus.api.spells;

import dev.cammiescorner.arcanus.common.components.entity.CurrentSpellComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class Spell {
	public static final Spell EMPTY = new Spell(AuraType.NONE, SpellComplexity.UNIQUE, 0, true) {
		@Override
		public void cast(World world, LivingEntity entity, Vec3d pos) {

		}
	};

	private final AuraType spellType;
	private final SpellComplexity spellComplexity;
	private final int spellCooldown;
	private final boolean isInstant;

	public Spell(AuraType type, SpellComplexity complexity, int cooldown, boolean instant) {
		spellType = type;
		spellComplexity = complexity;
		spellCooldown = cooldown;
		isInstant = instant;
	}

	public AuraType getSpellType() {
		return spellType;
	}

	public SpellComplexity getSpellComplexity() {
		return spellComplexity;
	}

	public int getAuraCost() {
		return spellComplexity.getAuraCost();
	}

	public int getSpellCooldown() {
		return spellCooldown;
	}

	public boolean isInstant() {
		return isInstant;
	}

	public boolean isActive(LivingEntity entity) {
		CurrentSpellComponent spellComponent = ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity);

		return spellComponent.getActiveSpell().equals(this);
	}

	public abstract void cast(World world, LivingEntity entity, Vec3d pos);
}

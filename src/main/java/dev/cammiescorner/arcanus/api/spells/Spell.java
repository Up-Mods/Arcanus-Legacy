package dev.cammiescorner.arcanus.api.spells;

import dev.cammiescorner.arcanus.common.components.entity.CurrentSpellComponent;
import dev.cammiescorner.arcanus.common.registry.ArcanusComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Spell {
	private final AuraType spellType;
	private final SpellComplexity spellComplexity;
	private final int spellCooldown;

	public Spell(AuraType type, SpellComplexity complexity, int cooldown) {
		spellType = type;
		spellComplexity = complexity;
		spellCooldown = cooldown;
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

	public boolean isActive(LivingEntity entity) {
		CurrentSpellComponent spellComponent = ArcanusComponents.CURRENT_SPELL_COMPONENT.get(entity);

		return spellComponent.getCastSpell().equals(this);
	}

	public void cast(World world, LivingEntity entity, Vec3d pos) {

	}

}

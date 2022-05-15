package dev.cammiescorner.arcanus.api.events.common;

import dev.cammiescorner.arcanus.api.spells.Spell;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UniqueSpellCallback {
	public static final Event<ActivateDeactivateSpell> EVENT = EventFactory.createArrayBacked(ActivateDeactivateSpell.class, callbacks -> (spell, world, entity, pos) -> {
		for(ActivateDeactivateSpell callback : callbacks)
			callback.run(spell, world, entity, pos);
	});

	public interface ActivateDeactivateSpell {
		void run(Spell spell, World world, LivingEntity entity, Vec3d pos);
	}
}

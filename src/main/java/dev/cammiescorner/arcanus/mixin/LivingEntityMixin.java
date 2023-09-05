package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.component.ArcanusComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
        throw new UnsupportedOperationException();
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    public Vec3 invertInput(Vec3 movementInput) {
        if (this.getComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED).isDiscombobulated()) {
            movementInput = movementInput.multiply(-1, 1, -1);
        }

        return movementInput;
    }
}

package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.component.ArcanusComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new UnsupportedOperationException();
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    public Vec3d invertInput(Vec3d movementInput) {
        if (this.getComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED).isDiscombobulated())
            movementInput = movementInput.multiply(-1, 1, -1);

        return movementInput;
    }
}

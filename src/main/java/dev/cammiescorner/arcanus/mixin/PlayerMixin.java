package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        info.getReturnValue().add(ArcanusEntityAttributes.MANA_COST).add(ArcanusEntityAttributes.MANA_REGEN).add(ArcanusEntityAttributes.BURNOUT_REGEN).add(ArcanusEntityAttributes.MANA_LOCK);
    }
}

package dev.cammiescorner.arcanus.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
        throw new UnsupportedOperationException();
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createAttributes(AttributeSupplier.Builder builder) {
        if (!ArcanusEntityAttributes.isInitialized()) return builder;
        return builder.add(ArcanusEntityAttributes.MANA_COST.get()).add(ArcanusEntityAttributes.MANA_REGEN.get()).add(ArcanusEntityAttributes.BURNOUT_REGEN.get()).add(ArcanusEntityAttributes.MANA_LOCK.get());
    }
}

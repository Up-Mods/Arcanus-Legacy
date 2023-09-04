package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Map;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private boolean affectCurrentAttribute;

    @ModifyVariable(method = "getTooltip", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;")), at = @At(value = "STORE"), ordinal = 0)
    private Map.Entry<EntityAttribute, EntityAttributeModifier> captureEntry(Map.Entry<EntityAttribute, EntityAttributeModifier> entry) {
        affectCurrentAttribute = ArcanusHelper.INVERSE_ENTITY_ATTRIBUTES.contains(entry.getKey());
        return entry;
    }

    @ModifyArg(method = "getTooltip", slice = @Slice(from = @At(value = "CONSTANT", args = "doubleValue=0.0", ordinal = 0)), at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 0))
    private Formatting changePositiveFormatting(Formatting value) {
        return affectCurrentAttribute ? Formatting.RED : value;
    }

    @ModifyArg(method = "getTooltip", slice = @Slice(from = @At(value = "CONSTANT", args = "doubleValue=0.0", ordinal = 1)), at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 0))
    private Formatting changeNegativeFormatting(Formatting value) {
        return affectCurrentAttribute ? Formatting.BLUE : value;
    }
}

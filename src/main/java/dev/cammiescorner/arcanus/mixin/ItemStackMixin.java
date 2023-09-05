package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.util.ArcanusHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
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

    @ModifyVariable(method = "getTooltipLines", slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;")), at = @At(value = "STORE"), ordinal = 0)
    private Map.Entry<Attribute, AttributeModifier> captureEntry(Map.Entry<Attribute, AttributeModifier> entry) {
        affectCurrentAttribute = ArcanusHelper.INVERSE_ENTITY_ATTRIBUTES.contains(entry.getKey());
        return entry;
    }

    @ModifyArg(method = "getTooltipLines", slice = @Slice(from = @At(value = "CONSTANT", args = "doubleValue=0.0", ordinal = 0)), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private ChatFormatting changePositiveFormatting(ChatFormatting value) {
        return affectCurrentAttribute ? ChatFormatting.RED : value;
    }

    @ModifyArg(method = "getTooltipLines", slice = @Slice(from = @At(value = "CONSTANT", args = "doubleValue=0.0", ordinal = 1)), at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0))
    private ChatFormatting changeNegativeFormatting(ChatFormatting value) {
        return affectCurrentAttribute ? ChatFormatting.BLUE : value;
    }
}

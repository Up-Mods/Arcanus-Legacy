package dev.cammiescorner.arcanus.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = WrittenBookItem.class, priority = 1100)
public abstract class WrittenBookItemMixin extends Item {

    private WrittenBookItemMixin(Properties settings) {
        super(settings);
        throw new UnsupportedOperationException();
    }

    @Inject(method = "makeSureTagIsValid", at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I"), cancellable = true)
    private static void isValid(CompoundTag nbt, CallbackInfoReturnable<Boolean> info) {
        String string = nbt.getString("title");
        info.setReturnValue(string.length() <= 40 && nbt.contains("author", 8));
    }

    @Inject(method = "use", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info, ItemStack stack) {
        if (!world.isClientSide() && stack.getOrCreateTag().contains("spell", Tag.TAG_STRING)) {
            Spell spell = Arcanus.SPELL.get(new ResourceLocation(stack.getOrCreateTag().getString("spell")));
            if (spell == null) {
                Arcanus.LOGGER.error("Spell {} does not exist!", stack.getOrCreateTag().getString("spell"));
                return;
            }

            user.getComponent(ArcanusComponents.SPELL_MEMORY).unlockSpell(spell);
        }
    }

    @Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void getName(ItemStack stack, CallbackInfoReturnable<Component> info, CompoundTag nbtCompound, String string) {
        if (stack.hasTag() && stack.getTag().contains("spell")) {
            info.setReturnValue(Component.translatable(string));
        }
    }

    @Inject(method = "appendHoverText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void appendTooltip(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context, CallbackInfo info, CompoundTag nbtCompound, String string) {
        if (stack.hasTag() && stack.getTag().contains("spell")) {
            tooltip.add(Component.translatableWithFallback("book.byAuthor", null, Component.translatable(string)).withStyle(ChatFormatting.GRAY));
            info.cancel();
        }
    }
}

package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

@Mixin(WrittenBookItem.class)
public abstract class WrittenBookItemMixin extends Item
{
	public WrittenBookItemMixin(Properties settings) { super(settings); }

	@Inject(method = "use", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info, ItemStack stack)
	{
		if(!world.isClientSide())
			((MagicUser) user).setKnownSpell(new ResourceLocation(stack.getOrCreateTag().getString("spell")));
	}

	@Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/LiteralText;<init>(Ljava/lang/String;)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void getName(ItemStack stack, CallbackInfoReturnable<Component> info, CompoundTag compoundTag, String string)
	{
		if(stack.getOrCreateTag().contains("spell"))
		{
			info.setReturnValue(new TranslatableComponent(string));
		}
	}

	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void appendTooltip(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context, CallbackInfo info, CompoundTag compoundTag, String string)
	{
		if(stack.getOrCreateTag().contains("spell"))
		{
			tooltip.add(new TranslatableComponent("book.byAuthor").append(new TranslatableComponent(string)).withStyle(ChatFormatting.GRAY));
			info.cancel();
		}
	}
}

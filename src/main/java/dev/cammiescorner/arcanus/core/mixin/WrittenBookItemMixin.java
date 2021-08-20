package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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
	public WrittenBookItemMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "use", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info, ItemStack stack) {
		if(!world.isClient())
			((MagicUser) user).setKnownSpell(new Identifier(stack.getOrCreateNbt().getString("spell")));
	}

	@Inject(method = "getName", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/LiteralText;<init>(Ljava/lang/String;)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void getName(ItemStack stack, CallbackInfoReturnable<Text> info, NbtCompound nbtCompound, String string) {
		if(stack.hasNbt() && stack.getNbt().contains("spell")) {
			info.setReturnValue(new TranslatableText(string));
		}
	}

	@Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info, NbtCompound nbtCompound, String string) {
		if(stack.hasNbt() && stack.getNbt().contains("spell")) {
			tooltip.add(new TranslatableText("book.byAuthor").append(new TranslatableText(string)).formatted(Formatting.GRAY));
			info.cancel();
		}
	}

	@Inject(method = "isValid", at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I"), cancellable = true)
	private static void isValid(NbtCompound nbt, CallbackInfoReturnable<Boolean> info) {
		String string = nbt.getString("title");
		info.setReturnValue(string.length() <= 40 && nbt.contains("author", 8));
	}
}

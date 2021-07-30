package dev.cammiescorner.arcanus.core.mixin.client;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.WandItem;
import dev.cammiescorner.arcanus.common.packets.CastSpellMessage;
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.ClientUtils;
import dev.cammiescorner.arcanus.core.util.Pattern;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;

@Mixin(Minecraft.class)
public class MinecraftClientMixin implements ClientUtils
{
	@Unique
	private boolean unfinishedSpell = true;
	@Unique
	private int timer = 0;
	@Unique
	private List<Pattern> pattern = new ArrayList<>(3);

	@Shadow
	@Nullable
	public LocalPlayer player;

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo info)
	{
		if(timer == 0 && !pattern.isEmpty())
			pattern.clear();

		if(player == null)
			return;

		if(player.getMainHandItem().getItem() instanceof WandItem)
		{
			if(timer > 0)
			{
				MutableComponent hyphen = new TextComponent("-").withStyle(ChatFormatting.GRAY);

				player.displayClientMessage(Arcanus.getSpellInputs(pattern, 0).append(hyphen).append(Arcanus.getSpellInputs(pattern, 1)).append(hyphen).append(Arcanus.getSpellInputs(pattern, 2)), true);

				if(pattern.size() == 3)
				{
					for(Spell spell : Arcanus.SPELL)
					{
						if(pattern.equals(spell.getSpellPattern()))
						{
							CastSpellMessage.send(Arcanus.SPELL.getKey(spell).toString());
							unfinishedSpell = false;
							break;
						}
						else
						{
							if(Arcanus.SPELL.getId(spell) + 1 == Arcanus.SPELL.keySet().size())
							{
								player.displayClientMessage(new TranslatableComponent("error." + Arcanus.MOD_ID + ".missing_spell").withStyle(ChatFormatting.RED), true);
								unfinishedSpell = false;
							}
						}
					}

					timer = 0;
				}
			}
			else if(pattern.size() < 3 && unfinishedSpell)
				player.displayClientMessage(new TextComponent(""), true);
		}
		else
			timer = 0;

		if(timer > 0)
			timer--;
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doItemUse()V", ordinal = 0), cancellable = true)
	public void onRightClick(CallbackInfo info)
	{
		if(player != null && player.getMainHandItem().getItem() instanceof WandItem)
		{
			timer = 20;
			unfinishedSpell = true;
			pattern.add(Pattern.RIGHT);
			player.swing(InteractionHand.MAIN_HAND);
			player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.1F);
			info.cancel();
		}
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;doAttack()V", ordinal = 0), cancellable = true)
	public void onLeftClick(CallbackInfo info)
	{
		if(player != null && player.getMainHandItem().getItem() instanceof WandItem)
		{
			timer = 20;
			unfinishedSpell = true;
			pattern.add(Pattern.LEFT);
			player.swing(InteractionHand.MAIN_HAND);
			player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.3F);
			info.cancel();
		}
	}

	@Override
	public List<Pattern> getPattern()
	{
		return pattern;
	}

	@Override
	public void setTimer(int value)
	{
		timer = value;
	}

	@Override
	public void setUnfinishedSpell(boolean value)
	{
		unfinishedSpell = value;
	}
}

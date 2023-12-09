package dev.cammiescorner.arcanus.mixin.client;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.net.CastSpellPacket;
import dev.cammiescorner.arcanus.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Unique
    private final List<Spell.Pattern> pattern = new ArrayList<>(3);
    @Shadow
    @Nullable
    public LocalPlayer player;
    @Unique
    private boolean unfinishedSpell = true;
    @Unique
    private int timer = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (timer == 0 && !pattern.isEmpty())
            pattern.clear();

        if (player == null)
            return;

        if (player.getMainHandItem().getItem() instanceof WandItem) {
            if (timer > 0) {
                MutableComponent hyphen = Component.literal("-").withStyle(ChatFormatting.GRAY);

                player.displayClientMessage(Arcanus.getSpellInputs(pattern, 0).append(hyphen).append(Arcanus.getSpellInputs(pattern, 1)).append(hyphen).append(Arcanus.getSpellInputs(pattern, 2)), true);

                if (pattern.size() == 3) {
                    for (Spell spell : Arcanus.SPELL) {
                        if (pattern.equals(spell.getSpellPattern())) {
                            CastSpellPacket.send(Arcanus.SPELL.getId(spell));
                            unfinishedSpell = false;
                            break;
                        } else {
                            if (Arcanus.SPELL.getId(spell) + 1 == Arcanus.SPELL.keySet().size()) {
                                player.displayClientMessage(Arcanus.translate("error", "missing_spell").withStyle(ChatFormatting.RED), true);
                                unfinishedSpell = false;
                            }
                        }
                    }

                    timer = 0;
                }
            } else if (pattern.size() < 3 && unfinishedSpell)
                player.displayClientMessage(Component.literal(""), true);
        } else
            timer = 0;

        if (timer > 0)
            timer--;
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startUseItem()V", ordinal = 0), cancellable = true)
    public void onRightClick(CallbackInfo info) {
        if (player != null && !player.isSpectator() && player.getMainHandItem().getItem() instanceof WandItem) {
            timer = 20;
            unfinishedSpell = true;
            pattern.add(Spell.Pattern.RIGHT);
            player.swing(InteractionHand.MAIN_HAND);
            player.level().playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.1F, 0L);
            info.cancel();
        }
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startAttack()Z", ordinal = 0), cancellable = true)
    public void onLeftClick(CallbackInfo info) {
        if (player != null && !player.isSpectator() && player.getMainHandItem().getItem() instanceof WandItem) {
            timer = 20;
            unfinishedSpell = true;
            pattern.add(Spell.Pattern.LEFT);
            player.swing(InteractionHand.MAIN_HAND);
            player.level().playSeededSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.PLAYERS, 1F, 1.3F, 0L);
            info.cancel();
        }
    }
}

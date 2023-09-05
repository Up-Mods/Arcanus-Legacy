package dev.cammiescorner.arcanus.net;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.item.WandItem;
import dev.cammiescorner.arcanus.registry.ArcanusDamageTypes;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class CastSpellPacket {
    public static final ResourceLocation ID = Arcanus.id("cast_spell");

    public static void send(int spellId) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(spellId);

        ClientPlayNetworking.send(ID, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        int spellId = buf.readVarInt();
        Spell spell = Arcanus.SPELL.byId(spellId);
        if (spell == null) {
            Arcanus.LOGGER.error("Received unknown spell id {} from player {}", spellId, player.getGameProfile().getName());
            return;
        }

        server.execute(() -> {
            MagicCaster caster = player.getComponent(ArcanusComponents.MAGIC_CASTER);

            if (player.getComponent(ArcanusComponents.SPELL_MEMORY).hasSpell(spell)) {
                int realManaCost = (int) (spell.getManaCost() * ArcanusEntityAttributes.getManaCost(player));

                if (player.isCreative() || (ArcanusConfig.haveBurnout && caster.getMana() > 0) || (!ArcanusConfig.haveBurnout && caster.getMana() >= realManaCost)) {
                    player.displayClientMessage(Component.translatable(spell.getTranslationKey()).withStyle(ChatFormatting.GREEN), true);
                    if(!caster.cast(spell)) {
                        return;
                    }
                    caster.setLastCastTime(player.level.getGameTime());

                    if (!player.isCreative()) {
                        if (caster.getMana() < realManaCost && ArcanusConfig.haveBurnout) {
                            int burnoutAmount = realManaCost - caster.getMana();
                            caster.addBurnout(burnoutAmount);
                            player.hurt(ArcanusDamageTypes.burnout(player.level), burnoutAmount);
                            player.displayClientMessage(Arcanus.translate("error", "burnout").withStyle(ChatFormatting.RED), false);
                        }

                        caster.drainMana(realManaCost);
                    }
                    player.syncComponent(ArcanusComponents.MAGIC_CASTER);

                    ItemStack stack = player.getMainHandItem();
                    WandItem wand = (WandItem) stack.getItem();
                    if (wand.hasUpgrade()) {
                        CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MOD_ID);
                        tag.putInt("Exp", tag.getInt("Exp") + realManaCost);

                        if (tag.getInt("Exp") >= wand.getMaxExp()) {
                            ItemStack newStack = new ItemStack(wand.getUpgrade());
                            tag = newStack.getOrCreateTagElement(Arcanus.MOD_ID);
                            tag.putInt("Exp", wand.getMaxExp());
                            player.setItemInHand(InteractionHand.MAIN_HAND, newStack);
                        }
                    }
                } else {
                    player.displayClientMessage(Arcanus.translate("error", "not_enough_mana").withStyle(ChatFormatting.RED), false);
                }
            } else {
                player.displayClientMessage(Arcanus.translate("error", "unknown_spell").withStyle(ChatFormatting.RED), true);
            }
        });
    }
}

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class CastSpellPacket {
    public static final Identifier ID = Arcanus.id("cast_spell");

    public static void send(int spellId) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeVarInt(spellId);

        ClientPlayNetworking.send(ID, buf);
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int spellId = buf.readVarInt();
        Spell spell = Arcanus.SPELL.get(spellId);
        if (spell == null) {
            Arcanus.LOGGER.error("Received unknown spell id {} from player {}", spellId, player.getGameProfile().getName());
            return;
        }

        server.execute(() -> {
            MagicCaster caster = player.getComponent(ArcanusComponents.MAGIC_CASTER);

            if (player.getComponent(ArcanusComponents.SPELL_MEMORY).hasSpell(spell)) {
                int realManaCost = (int) (spell.getManaCost() * ArcanusEntityAttributes.getManaCost(player));

                if (player.isCreative() || (ArcanusConfig.haveBurnout && caster.getMana() > 0) || (!ArcanusConfig.haveBurnout && caster.getMana() >= realManaCost)) {
                    player.sendMessage(Text.translatable(spell.getTranslationKey()).formatted(Formatting.GREEN), true);
                    spell.onCast(player.world, player);

                    if (!player.isCreative()) {
                        caster.setLastCastTime(player.world.getTime());

                        if (caster.getMana() < realManaCost && ArcanusConfig.haveBurnout) {
                            int burnoutAmount = realManaCost - caster.getMana();
                            caster.addBurnout(burnoutAmount);
                            player.damage(ArcanusDamageTypes.burnout(player.world), burnoutAmount);
                            player.sendMessage(Arcanus.translate("error", "burnout").formatted(Formatting.RED), false);
                        }

                        caster.addMana(-realManaCost);
                        player.syncComponent(ArcanusComponents.MAGIC_CASTER);
                    }

                    ItemStack stack = player.getMainHandStack();
                    WandItem wand = (WandItem) stack.getItem();
                    if (wand.hasUpgrade()) {
                        NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
                        tag.putInt("Exp", tag.getInt("Exp") + realManaCost);

                        if (tag.getInt("Exp") >= wand.getMaxExp()) {
                            ItemStack newStack = new ItemStack(wand.getUpgrade());
                            tag = newStack.getOrCreateSubNbt(Arcanus.MOD_ID);
                            tag.putInt("Exp", wand.getMaxExp());
                            player.setStackInHand(Hand.MAIN_HAND, newStack);
                        }
                    }
                } else {
                    player.sendMessage(Arcanus.translate("error", "not_enough_mana").formatted(Formatting.RED), false);
                }
            } else {
                player.sendMessage(Arcanus.translate("error", "unknown_spell").formatted(Formatting.RED), true);
            }
        });
    }
}

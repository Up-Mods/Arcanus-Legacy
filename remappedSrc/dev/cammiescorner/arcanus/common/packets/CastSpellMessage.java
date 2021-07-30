package dev.cammiescorner.arcanus.common.packets;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import static dev.cammiescorner.arcanus.Arcanus.config;

public class CastSpellMessage
{
	public static final ResourceLocation ID = new ResourceLocation(Arcanus.MOD_ID, "cast_spell");

	public static void send(String spellId)
	{
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(spellId);

		ClientSidePacketRegistryImpl.INSTANCE.sendToServer(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl network, FriendlyByteBuf buf, PacketSender sender)
	{
		String spellId = buf.readUtf();

		server.execute(() ->
		{
			MagicUser user = (MagicUser) player;
			Spell spell = Arcanus.SPELL.get(new ResourceLocation(spellId));

			if(user.getKnownSpells().contains(spell))
			{
				if((config.doBurnout && user.getMana() > 0) || (!config.doBurnout && user.getMana() >= spell.getManaCost()))
				{
					player.displayClientMessage(new TranslatableComponent(spell.getTranslationKey()).withStyle(ChatFormatting.GREEN), true);
					spell.onCast(player.level, player);
					user.setLastCastTime(player.level.getGameTime());

					if(user.getMana() < spell.getManaCost() && config.doBurnout)
					{
						user.addBurnout(spell.getManaCost() - user.getMana());
						player.displayClientMessage(new TranslatableComponent("error." + Arcanus.MOD_ID + ".burnout").withStyle(ChatFormatting.RED), false);
					}

					user.addMana(-spell.getManaCost());
				}
				else
				{
					player.displayClientMessage(new TranslatableComponent("error." + Arcanus.MOD_ID + ".not_enough_mana").withStyle(ChatFormatting.RED), false);
				}
			}
			else
			{
				player.displayClientMessage(new TranslatableComponent("error." + Arcanus.MOD_ID + ".unknown_spell").withStyle(ChatFormatting.RED), true);
			}
		});
	}
}

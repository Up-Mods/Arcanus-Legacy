package dev.cammiescorner.arcanus.common.packets;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.impl.networking.ClientSidePacketRegistryImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CastSpellMessage
{
	public static final Identifier ID = new Identifier(Arcanus.MOD_ID, "cast_spell");

	public static void send(String spellId)
	{
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(spellId);

		ClientSidePacketRegistryImpl.INSTANCE.sendToServer(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender)
	{
		String spellId = buf.readString();

		server.execute(() ->
		{
			MagicUser user = (MagicUser) player;
			Spell spell = Arcanus.SPELL.get(new Identifier(spellId));

			if(user.getKnownSpells().contains(spell))
			{
				if(user.getBurnout() <= 0)
				{
					player.sendMessage(new TranslatableText(spell.getTranslationKey()).formatted(Formatting.GREEN), true);
					spell.onCast(player.world, player);
					user.setLastCastTime(player.world.getTime());

					if(user.getMana() < spell.getManaCost())
					{
						user.addBurnout(spell.getManaCost() - user.getMana());
						player.sendMessage(new TranslatableText("error." + Arcanus.MOD_ID + ".burnout").formatted(Formatting.RED), true);
					}

					user.addMana(-spell.getManaCost());
				}
				else
				{
					player.sendMessage(new TranslatableText("error." + Arcanus.MOD_ID + ".burnout").formatted(Formatting.RED), true);
				}
			}
			else
			{
				player.sendMessage(new TranslatableText("error." + Arcanus.MOD_ID + ".unknown_spell").formatted(Formatting.RED), true);
			}
		});
	}
}

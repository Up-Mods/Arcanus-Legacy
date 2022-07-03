package dev.cammiescorner.arcanus.common.packets;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.items.WandItem;
import dev.cammiescorner.arcanus.core.registry.ModDamageSource;
import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.Spell;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
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

import static dev.cammiescorner.arcanus.Arcanus.*;

public class CastSpellPacket {
	public static final Identifier ID = new Identifier(Arcanus.MOD_ID, "cast_spell");

	public static void send(int spellId) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(spellId);

		ClientPlayNetworking.send(ID, buf);
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int spellId = buf.readVarInt();

		server.execute(() -> {
			MagicUser user = (MagicUser) player;
			ItemStack stack = player.getMainHandStack();
			WandItem wand = (WandItem) stack.getItem();
			Spell spell = Arcanus.SPELL.get(spellId);

			if(user.getKnownSpells().contains(spell)) {
				int realManaCost = (int) (spell.getManaCost() * ArcanusHelper.getManaCost(player));

				if(player.isCreative() || (getConfig().haveBurnout && user.getMana() > 0) || (!getConfig().haveBurnout && user.getMana() >= realManaCost)) {
					player.sendMessage(Text.translatable(spell.getTranslationKey()).formatted(Formatting.GREEN), true);
					spell.onCast(player.world, player);

					if(!player.isCreative()) {
						user.setLastCastTime(player.world.getTime());

						if(user.getMana() < realManaCost && getConfig().haveBurnout) {
							int burnoutAmount = realManaCost - user.getMana();
							user.addBurnout(burnoutAmount);
							player.damage(ModDamageSource.MAGIC_BURNOUT, burnoutAmount);
							player.sendMessage(Text.translatable("error." + Arcanus.MOD_ID + ".burnout").formatted(Formatting.RED), false);
						}

						user.addMana(-realManaCost);
					}

					NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);

					if(wand.hasUpgrade()) {
						tag.putInt("Exp", tag.getInt("Exp") + realManaCost);

						if(tag.getInt("Exp") >= wand.getMaxExp()) {
							ItemStack newStack = new ItemStack(wand.getUpgrade());
							tag = newStack.getOrCreateSubNbt(Arcanus.MOD_ID);
							tag.putInt("Exp", wand.getMaxExp());
							player.setStackInHand(Hand.MAIN_HAND, newStack);
						}
					}
				}
				else {
					player.sendMessage(Text.translatable("error." + Arcanus.MOD_ID + ".not_enough_mana").formatted(Formatting.RED), false);
				}
			}
			else {
				player.sendMessage(Text.translatable("error." + Arcanus.MOD_ID + ".unknown_spell").formatted(Formatting.RED), true);
			}
		});
	}
}

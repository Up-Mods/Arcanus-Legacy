package dev.cammiescorner.arcanus.core.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.spells.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModCommands
{
	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated)
	{
		if(dedicated)
			ArgumentTypes.register("spells", SpellArgumentType.class, new ConstantArgumentSerializer<>(SpellArgumentType::new));

		dispatcher.register(CommandManager.literal("spells").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(3)).then(CommandManager.literal("list").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context ->
		{
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			String knownSpells = "";

			for(Spell spell : ((MagicUser) player).getKnownSpells())
				knownSpells = knownSpells.concat("\n    [" + Arcanus.SPELL.getId(spell)) + "]";

			if(!knownSpells.isEmpty())
				context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.list", player.getEntityName(), knownSpells), false);
			else
				context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.no_known_spells", player.getEntityName()));

			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("list").executes(context ->
		{
			PlayerEntity player = context.getSource().getPlayer();
			String knownSpells = "";

			for(Spell spell : ((MagicUser) player).getKnownSpells())
				knownSpells = knownSpells.concat("\n    [" + Arcanus.SPELL.getId(spell)) + "]";

			if(!knownSpells.isEmpty())
				context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.list", player.getEntityName(), knownSpells), false);
			else
				context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.no_known_spells", player.getEntityName()));

			return Command.SINGLE_SUCCESS;
		})).then(CommandManager.literal("add").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("spell", new SpellArgumentType()).executes(context ->
		{
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;
			Spell spell = SpellArgumentType.getSpell(context, "spell");

			user.getKnownSpells().add(spell);
			context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getId(spell), player.getEntityName()), false);

			return Command.SINGLE_SUCCESS;
		})))).then(CommandManager.literal("add").then(CommandManager.argument("spell", new SpellArgumentType()).executes(context ->
		{
			PlayerEntity player = context.getSource().getPlayer();
			MagicUser user = (MagicUser) player;
			Spell spell = SpellArgumentType.getSpell(context, "spell");

			user.getKnownSpells().add(spell);
			context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getId(spell), player.getEntityName()), false);

			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("add").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("all", StringArgumentType.word()).executes(context ->
		{
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;

			if(StringArgumentType.getString(context, "all").equals("all"))
			{
				user.getKnownSpells().clear();

				Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getId(spell)));
				context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.added_all", player.getEntityName()), false);
			}
			else
				context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

			return Command.SINGLE_SUCCESS;
		})))).then(CommandManager.literal("add").then(CommandManager.argument("all", StringArgumentType.word()).executes(context ->
		{
			PlayerEntity player = context.getSource().getPlayer();
			MagicUser user = (MagicUser) player;

			if(StringArgumentType.getString(context, "all").equals("all"))
			{
				user.getKnownSpells().clear();

				Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getId(spell)));
				context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.added_all", player.getEntityName()), false);
			}
			else
				context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("remove").then(CommandManager.argument("player", EntityArgumentType.player()).then(CommandManager.argument("spell", new SpellArgumentType()).executes(context ->
		{
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;
			Spell spell = SpellArgumentType.getSpell(context, "spell");

			for(Spell knownSpell : user.getKnownSpells())
			{
				if(knownSpell.equals(spell))
				{
					user.getKnownSpells().remove(spell);
					context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.removed", Arcanus.SPELL.getId(spell), player.getEntityName()), false);
				}
				else
				{
					context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.does_not_have", player.getEntityName(), Arcanus.SPELL.getId(spell)));
				}

				break;
			}

			return Command.SINGLE_SUCCESS;
		})))).then(CommandManager.literal("remove").then(CommandManager.argument("spell", new SpellArgumentType()).executes(context ->
		{
			PlayerEntity player = context.getSource().getPlayer();
			MagicUser user = (MagicUser) player;
			Spell spell = SpellArgumentType.getSpell(context, "spell");

			for(Spell knownSpell : user.getKnownSpells())
			{
				if(knownSpell.equals(spell))
				{
					user.getKnownSpells().remove(spell);
					context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.removed", Arcanus.SPELL.getId(spell), player.getEntityName()), false);
				}
				else
				{
					context.getSource().sendError(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.does_not_have", player.getEntityName(), Arcanus.SPELL.getId(spell)));
				}

				break;
			}

			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("clear").then(CommandManager.argument("player", EntityArgumentType.player()).executes(context ->
		{
			PlayerEntity player = EntityArgumentType.getPlayer(context, "player");

			((MagicUser) player).getKnownSpells().clear();
			context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.cleared", player.getEntityName()), false);

			return Command.SINGLE_SUCCESS;
		}))).then(CommandManager.literal("clear").executes(context ->
		{
			PlayerEntity player = context.getSource().getPlayer();

			((MagicUser) player).getKnownSpells().clear();
			context.getSource().sendFeedback(new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.cleared", player.getEntityName()), false);

			return Command.SINGLE_SUCCESS;
		})));
	}

	private static class SpellArgumentType implements ArgumentType<Spell>
	{
		public static final DynamicCommandExceptionType INVALID_SPELL_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.not_found", object));

		public static Spell getSpell(CommandContext<ServerCommandSource> commandContext, String string)
		{
			return commandContext.getArgument(string, Spell.class);
		}

		@Override
		public Spell parse(StringReader reader) throws CommandSyntaxException
		{
			Identifier identifier = Identifier.fromCommandInput(reader);
			return Arcanus.SPELL.getOrEmpty(identifier).orElseThrow(() -> INVALID_SPELL_EXCEPTION.create(identifier));
		}

		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
		{
			return CommandSource.suggestIdentifiers(Arcanus.SPELL.getIds(), builder);
		}
	}
}

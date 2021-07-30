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
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ModCommands
{
	public static void init(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated)
	{
		if(dedicated)
			ArgumentTypes.register("spells", SpellArgumentType.class, new EmptyArgumentSerializer<>(SpellArgumentType::new));

		dispatcher.register(Commands.literal("spells").requires(serverCommandSource -> serverCommandSource.hasPermission(3))
				.then(Commands.literal("list")
						.executes(SpellsCommand::listSelfSpells)
						.then(Commands.argument("player", EntityArgument.player())
								.executes(SpellsCommand::listPlayerSpells)))
				.then(Commands.literal("add").then(Commands.argument("all", StringArgumentType.word())
						.executes(SpellsCommand::addAllSpellsToSelf))
						.then(Commands.argument("spell", SpellArgumentType.spell())
								.executes(SpellsCommand::addSpellToSelf))
								.then(Commands.argument("player", EntityArgument.player())
										.then(Commands.argument("all", StringArgumentType.word())
											.executes(SpellsCommand::addAllSpellsToPlayer))
											.then(Commands.argument("spell", SpellArgumentType.spell())
													.executes(SpellsCommand::addSpellToPlayer))))
				.then(Commands.literal("remove").then(Commands.argument("all", StringArgumentType.word())
						.executes(SpellsCommand::removeAllSpellsFromSelf))
						.then(Commands.argument("spell", SpellArgumentType.spell())
							.executes(SpellsCommand::removeSpellFromSelf))
								.then(Commands.argument("player", EntityArgument.player())
										.then(Commands.argument("all", StringArgumentType.word())
											.executes(SpellsCommand::removeAllSpellsFromPlayer))
											.then(Commands.argument("spell", SpellArgumentType.spell())
												.executes(SpellsCommand::removeSpellFromPlayer)))));
	}

	private static class SpellArgumentType implements ArgumentType<Spell>
	{
		public static final DynamicCommandExceptionType INVALID_SPELL_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.not_found", object));

		public static SpellArgumentType spell()
		{
			return new SpellArgumentType();
		}

		public static Spell getSpell(CommandContext<CommandSourceStack> commandContext, String string)
		{
			return commandContext.getArgument(string, Spell.class);
		}

		@Override
		public Spell parse(StringReader reader) throws CommandSyntaxException
		{
			ResourceLocation identifier = ResourceLocation.read(reader);
			return Arcanus.SPELL.getOptional(identifier).orElseThrow(() -> INVALID_SPELL_EXCEPTION.create(identifier));
		}

		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
		{
			return SharedSuggestionProvider.suggestResource(Arcanus.SPELL.keySet(), builder);
		}
	}

	private static class SpellsCommand
	{
		public static int listSelfSpells(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = context.getSource().getPlayerOrException();
			String knownSpells = "";

			for(Spell spell : ((MagicUser) player).getKnownSpells())
				knownSpells = knownSpells.concat("\n    [" + Arcanus.SPELL.getKey(spell)) + "]";

			if(!knownSpells.isEmpty())
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.list", player.getScoreboardName(), knownSpells), false);
			else
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.no_known_spells", player.getScoreboardName()));

			return Command.SINGLE_SUCCESS;
		}

		public static int listPlayerSpells(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = EntityArgument.getPlayer(context, "player");
			String knownSpells = "";

			for(Spell spell : ((MagicUser) player).getKnownSpells())
				knownSpells = knownSpells.concat("\n    [" + Arcanus.SPELL.getKey(spell)) + "]";

			if(!knownSpells.isEmpty())
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.list", player.getScoreboardName(), knownSpells), false);
			else
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.no_known_spells", player.getScoreboardName()));

			return Command.SINGLE_SUCCESS;
		}

		public static int addAllSpellsToSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = context.getSource().getPlayerOrException();
			MagicUser user = (MagicUser) player;

			if(StringArgumentType.getString(context, "all").equals("all"))
			{
				Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getKey(spell)));
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.added_all", player.getScoreboardName()), false);
			}
			else
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

			return Command.SINGLE_SUCCESS;
		}

		public static int addSpellToSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = context.getSource().getPlayerOrException();
			MagicUser user = (MagicUser) player;
			Spell spell = ModCommands.SpellArgumentType.getSpell(context, "spell");

			if(!user.getKnownSpells().contains(spell))
			{
				user.getKnownSpells().add(spell);
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getKey(spell), player.getScoreboardName()), false);
			}
			else
			{
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.already_known", player.getScoreboardName(), Arcanus.SPELL.getKey(spell)));
			}

			return Command.SINGLE_SUCCESS;
		}

		public static int addAllSpellsToPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = EntityArgument.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;

			if(StringArgumentType.getString(context, "all").equals("all"))
			{
				Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getKey(spell)));
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.added_all", player.getScoreboardName()), false);
			}
			else
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

			return Command.SINGLE_SUCCESS;
		}

		public static int addSpellToPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = EntityArgument.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;
			Spell spell = ModCommands.SpellArgumentType.getSpell(context, "spell");

			if(!user.getKnownSpells().contains(spell))
			{
				user.getKnownSpells().add(spell);
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getKey(spell), player.getScoreboardName()), false);
			}
			else
			{
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.already_known", player.getScoreboardName(), Arcanus.SPELL.getKey(spell)));
			}

			return Command.SINGLE_SUCCESS;
		}

		public static int removeAllSpellsFromSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = context.getSource().getPlayerOrException();

			((MagicUser) player).getKnownSpells().clear();
			context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.cleared", player.getScoreboardName()), false);

			return Command.SINGLE_SUCCESS;
		}

		public static int removeSpellFromSelf(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = context.getSource().getPlayerOrException();
			MagicUser user = (MagicUser) player;
			Spell spell = ModCommands.SpellArgumentType.getSpell(context, "spell");

			if(user.getKnownSpells().contains(spell))
			{
				user.getKnownSpells().remove(spell);
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.removed", Arcanus.SPELL.getKey(spell), player.getScoreboardName()), false);
			}
			else
			{
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.does_not_have", player.getScoreboardName(), Arcanus.SPELL.getKey(spell)));
			}

			return Command.SINGLE_SUCCESS;
		}

		public static int removeAllSpellsFromPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = EntityArgument.getPlayer(context, "player");

			((MagicUser) player).getKnownSpells().clear();
			context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.cleared", player.getScoreboardName()), false);

			return Command.SINGLE_SUCCESS;
		}

		public static int removeSpellFromPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
		{
			Player player = EntityArgument.getPlayer(context, "player");
			MagicUser user = (MagicUser) player;
			Spell spell = ModCommands.SpellArgumentType.getSpell(context, "spell");

			if(user.getKnownSpells().contains(spell))
			{
				user.getKnownSpells().remove(spell);
				context.getSource().sendSuccess(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.removed", Arcanus.SPELL.getKey(spell), player.getScoreboardName()), false);
			}
			else
			{
				context.getSource().sendFailure(new TranslatableComponent("commands." + Arcanus.MOD_ID + ".spells.does_not_have", player.getScoreboardName(), Arcanus.SPELL.getKey(spell)));
			}

			return Command.SINGLE_SUCCESS;
		}
	}
}

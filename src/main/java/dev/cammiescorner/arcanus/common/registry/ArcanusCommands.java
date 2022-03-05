package dev.cammiescorner.arcanus.common.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.spells.Spell;
import dev.cammiescorner.arcanus.common.components.entity.SpellComponent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ArcanusCommands {
	public static void register() {
		ArgumentTypes.register("spell", SpellArgumentType.class, new ConstantArgumentSerializer<>(SpellArgumentType::new));
	}

	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		dispatcher.register(CommandManager.literal("spell")
				.then(CommandManager.literal("set")
						.then(CommandManager.argument("spell", SpellArgumentType.spell())
								.executes(SpellsCommand::setSpell)
						)
				)
				.then(CommandManager.literal("get")
						.executes(SpellsCommand::getSpell)
				)
		);
	}

	public static class SpellArgumentType implements ArgumentType<Spell> {
		public static final DynamicCommandExceptionType INVALID_SPELL_EXCEPTION = new DynamicCommandExceptionType(object -> new TranslatableText("commands." + Arcanus.MOD_ID + ".spells.not_found", object));

		public static SpellArgumentType spell() {
			return new SpellArgumentType();
		}

		public static Spell getSpell(CommandContext<ServerCommandSource> commandContext, String string) {
			return commandContext.getArgument(string, Spell.class);
		}

		@Override
		public Spell parse(StringReader reader) throws CommandSyntaxException {
			Identifier identifier = Identifier.fromCommandInput(reader);
			return Arcanus.SPELL.getOrEmpty(identifier).orElseThrow(() -> INVALID_SPELL_EXCEPTION.create(identifier));
		}

		@Override
		public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
			return CommandSource.suggestIdentifiers(Arcanus.SPELL.getIds(), builder);
		}
	}

	private static class SpellsCommand {
		public static int setSpell(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
			PlayerEntity player = context.getSource().getPlayer();
			SpellComponent spellComponent = ArcanusComponents.SPELL_COMPONENT.get(player);
			Spell spell = ArcanusCommands.SpellArgumentType.getSpell(context, "spell");

			spellComponent.setSpellInSlot(spell, 0);
			context.getSource().sendFeedback(new LiteralText("Set spell to: " + Arcanus.SPELL.getId(spell)), false);

			return Command.SINGLE_SUCCESS;
		}

		public static int getSpell(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
			PlayerEntity player = context.getSource().getPlayer();
			SpellComponent spellComponent = ArcanusComponents.SPELL_COMPONENT.get(player);
			Spell spell = spellComponent.getSelectedSpell();

			context.getSource().sendFeedback(new LiteralText("Spell is: " + Arcanus.SPELL.getId(spell)), false);

			return Command.SINGLE_SUCCESS;
		}
	}
}

package dev.cammiescorner.arcanus.core.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.Spell;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ModCommands {

    public static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext buildContext, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("spells")
                .then(CommandManager.literal("list").requires(source -> source.hasPermissionLevel(0))
                        .executes(context -> SpellsCommand.listPlayerSpells(context, context.getSource().getPlayer()))
                        .then(CommandManager.argument("player", EntityArgumentType.player()).requires(source -> source.hasPermissionLevel(3))
                                .executes(context -> SpellsCommand.listPlayerSpells(context, EntityArgumentType.getPlayer(context, "player")))))
                .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("all", StringArgumentType.word())
                                .executes(SpellsCommand::addAllSpellsToSelf))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(SpellsCommand::addSpellToSelf))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("all", StringArgumentType.word())
                                        .executes(SpellsCommand::addAllSpellsToPlayer))
                                .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                        .executes(SpellsCommand::addSpellToPlayer))))
                .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("all", StringArgumentType.word())
                                .executes(SpellsCommand::removeAllSpellsFromSelf))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(SpellsCommand::removeSpellFromSelf))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("all", StringArgumentType.word())
                                        .executes(SpellsCommand::removeAllSpellsFromPlayer))
                                .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                        .executes(SpellsCommand::removeSpellFromPlayer))))
                .then(CommandManager.literal("spellbook").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("all")
                                .executes(context -> SpellsCommand.giveSpellBook(context, context.getSource().getPlayer(), null))
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(context, EntityArgumentType.getPlayer(context, "player"), null))))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(context -> SpellsCommand.giveSpellBook(context, context.getSource().getPlayer(), RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value()))
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(context, EntityArgumentType.getPlayer(context, "player"), RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value()))))));
    }

    private static class SpellsCommand {
        public static int listPlayerSpells(CommandContext<ServerCommandSource> context, PlayerEntity player) throws CommandSyntaxException {
            if (((MagicUser) player).getKnownSpells().isEmpty()) {
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.no_known_spells", null, player.getDisplayName()));
                return 0;
            }

            MutableText knownSpells = Text.literal("");

            for (Spell spell : ((MagicUser) player).getKnownSpells())
                knownSpells = knownSpells.append("\n    - ").append(Text.translatable(spell.getTranslationKey())).append(" (" + spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol() + ")");

            context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.list", player.getEntityName(), knownSpells), false);

            return ((MagicUser) player).getKnownSpells().size();
        }

        public static int addAllSpellsToSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = context.getSource().getPlayer();
            MagicUser user = (MagicUser) player;

            if (StringArgumentType.getString(context, "all").equals("all")) {
                Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getId(spell)));
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.added_all", null, player.getDisplayName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

            return Command.SINGLE_SUCCESS;
        }

        public static int addSpellToSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = context.getSource().getPlayer();
            MagicUser user = (MagicUser) player;
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (!user.getKnownSpells().contains(spell)) {
                user.getKnownSpells().add(spell);
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getId(spell), player.getEntityName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.already_known", player.getEntityName(), Arcanus.SPELL.getId(spell)));

            return Command.SINGLE_SUCCESS;
        }

        public static int addAllSpellsToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            MagicUser user = (MagicUser) player;

            if (StringArgumentType.getString(context, "all").equals("all")) {
                Arcanus.SPELL.forEach(spell -> user.setKnownSpell(Arcanus.SPELL.getId(spell)));
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.added_all", null, player.getDisplayName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.not_valid_spell"));

            return Command.SINGLE_SUCCESS;
        }

        public static int addSpellToPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            MagicUser user = (MagicUser) player;
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (!user.getKnownSpells().contains(spell)) {
                user.getKnownSpells().add(spell);
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.added", Arcanus.SPELL.getId(spell), player.getEntityName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.already_known", player.getEntityName(), Arcanus.SPELL.getId(spell)));

            return Command.SINGLE_SUCCESS;
        }

        public static int removeAllSpellsFromSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = context.getSource().getPlayer();

            ((MagicUser) player).getKnownSpells().clear();
            context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.cleared", null, player.getDisplayName()), false);

            return Command.SINGLE_SUCCESS;
        }

        public static int removeSpellFromSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = context.getSource().getPlayer();
            MagicUser user = (MagicUser) player;
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (user.getKnownSpells().contains(spell)) {
                user.getKnownSpells().remove(spell);
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.removed", null, Arcanus.SPELL.getId(spell), player.getDisplayName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.does_not_have", null, player.getDisplayName(), Arcanus.SPELL.getId(spell)));

            return Command.SINGLE_SUCCESS;
        }

        public static int removeAllSpellsFromPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            ((MagicUser) player).getKnownSpells().clear();
            context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.cleared", null, player.getDisplayName()), false);

            return Command.SINGLE_SUCCESS;
        }

        public static int removeSpellFromPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            MagicUser user = (MagicUser) player;
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (user.getKnownSpells().contains(spell)) {
                user.getKnownSpells().remove(spell);
                context.getSource().sendFeedback(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.removed", null, Arcanus.SPELL.getId(spell), player.getDisplayName()), false);
            } else
                context.getSource().sendError(Text.translatable("commands." + Arcanus.MOD_ID + ".spells.does_not_have", null, player.getDisplayName(), Arcanus.SPELL.getId(spell)));

            return Command.SINGLE_SUCCESS;
        }

        public static int giveSpellBook(CommandContext<ServerCommandSource> context, ServerPlayerEntity player, Spell spell) {
            if (spell == null) {
                Arcanus.SPELL.forEach(spell1 -> SpellsCommand.giveSpellBook(player, spell1));

                return Command.SINGLE_SUCCESS;
            }

            giveSpellBook(player, spell);

            return Command.SINGLE_SUCCESS;
        }

        public static void giveSpellBook(ServerPlayerEntity player, Spell spell) {
            ItemStack book = SpellBooks.getSpellBook(spell, player.getRandom());
            ArcanusHelper.giveOrDrop(player, book);
        }
    }
}

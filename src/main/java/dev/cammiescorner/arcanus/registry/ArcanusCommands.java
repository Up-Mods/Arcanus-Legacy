package dev.cammiescorner.arcanus.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.SpellMemory;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusHelper;
import dev.cammiescorner.arcanus.util.SpellBooks;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import java.util.Comparator;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ArcanusCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(Commands.literal("spells")
                .then(Commands.literal("list").requires(source -> source.hasPermission(0))
                        .executes(context -> SpellsCommand.listPlayerSpells(context, context.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player()).requires(source -> source.hasPermission(3))
                                .executes(context -> SpellsCommand.listPlayerSpells(context, EntityArgument.getPlayer(context, "player")))))
                .then(Commands.literal("add").requires(source -> source.hasPermission(3))
                        .then(Commands.argument("all", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    return SpellsCommand.addAllSpellsToPlayer(ctx, player);
                                }))
                        .then(Commands.argument("spell", ResourceArgument.resource(buildContext, Arcanus.SPELL.key()))
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    return SpellsCommand.addSpellToPlayer(ctx, player);
                                }))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("all", StringArgumentType.word())
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            return SpellsCommand.addAllSpellsToPlayer(ctx, player);
                                        }))
                                .then(Commands.argument("spell", ResourceArgument.resource(buildContext, Arcanus.SPELL.key()))
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            return SpellsCommand.addSpellToPlayer(ctx, player);
                                        }))))
                .then(Commands.literal("remove").requires(source -> source.hasPermission(3))
                        .then(Commands.argument("all", StringArgumentType.word())
                                .executes(ctx -> SpellsCommand.removeAllSpellsFromPlayer(ctx, ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("spell", ResourceArgument.resource(buildContext, Arcanus.SPELL.key()))
                                .executes(ctx -> SpellsCommand.removeSpellFromPlayer(ctx, ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("all", StringArgumentType.word())
                                        .executes(ctx -> SpellsCommand.removeAllSpellsFromPlayer(ctx, EntityArgument.getPlayer(ctx, "player"))))
                                .then(Commands.argument("spell", ResourceArgument.resource(buildContext, Arcanus.SPELL.key()))
                                        .executes(ctx -> SpellsCommand.removeSpellFromPlayer(ctx, EntityArgument.getPlayer(ctx, "player"))))))
                .then(Commands.literal("spellbook").requires(source -> source.hasPermission(2))
                        .then(Commands.literal("all")
                                .executes(context -> SpellsCommand.giveAllSpellBooks(context.getSource().getPlayerOrException()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(EntityArgument.getPlayer(context, "player"), ResourceArgument.getResource(context, "spell", Arcanus.SPELL_KEY).value())))
                        .then(Commands.argument("spell", ResourceArgument.resource(buildContext, Arcanus.SPELL.key()))
                                .executes(context -> SpellsCommand.giveSpellBook(context.getSource().getPlayerOrException(), ResourceArgument.getResource(context, "spell", Arcanus.SPELL_KEY).value()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(EntityArgument.getPlayer(context, "player"), ResourceArgument.getResource(context, "spell", Arcanus.SPELL_KEY).value())))))))));
    }

    private static class SpellsCommand {
        public static int listPlayerSpells(CommandContext<CommandSourceStack> context, Player player) {
            Set<Spell> spells = player.getComponent(ArcanusComponents.SPELL_MEMORY).getKnownSpells();
            if (spells.isEmpty()) {
                context.getSource().sendFailure(Arcanus.translate("commands", "spells.no_known_spells", player.getDisplayName()));
                return 0;
            }

            MutableComponent knownSpells = Component.literal("");
            spells.stream().sorted(Comparator.comparing(spell -> Arcanus.SPELL.getKey(spell).toString())).forEachOrdered(spell -> knownSpells.append("\n    - ").append(Component.translatable(spell.getTranslationKey())).append(" (" + spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol() + ")"));

            context.getSource().sendSuccess(Arcanus.translate("commands", "spells.list", player.getScoreboardName(), knownSpells), false);
            return spells.size();
        }

        public static int addAllSpellsToPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            int count = (int) Arcanus.SPELL.stream().filter(memory::unlockSpell).count();
            player.syncComponent(ArcanusComponents.SPELL_MEMORY);
            context.getSource().sendSuccess(Arcanus.translate("commands", "spells.added_all", player.getDisplayName()), false);
            return Math.max(count, 1);
        }

        public static int addSpellToPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            Spell spell = ResourceArgument.getResource(context, "spell", Arcanus.SPELL_KEY).value();

            if (memory.unlockSpell(spell)) {
                context.getSource().sendSuccess(Arcanus.translate("commands", "spells.added", Component.translatable(spell.getTranslationKey(), Arcanus.SPELL.getKey(spell)), player.getScoreboardName()), false);
                player.syncComponent(ArcanusComponents.SPELL_MEMORY);
                return Command.SINGLE_SUCCESS;
            }

            context.getSource().sendFailure(Arcanus.translate("commands", "spells.already_known", player.getScoreboardName(), Component.translatable(spell.getTranslationKey(), Arcanus.SPELL.getKey(spell))));
            return 0;
        }

        public static int removeAllSpellsFromPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            memory.clear();
            player.syncComponent(ArcanusComponents.SPELL_MEMORY);
            context.getSource().sendSuccess(Arcanus.translate("commands", "spells.cleared", player.getDisplayName()), false);
            return Command.SINGLE_SUCCESS;
        }

        public static int removeSpellFromPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            Spell spell = ResourceArgument.getResource(context, "spell", Arcanus.SPELL_KEY).value();

            if (memory.removeSpell(spell)) {
                context.getSource().sendSuccess(Arcanus.translate("commands", "spells.removed", Component.translatable(spell.getTranslationKey(), Arcanus.SPELL.getKey(spell)), player.getDisplayName()), false);
                player.syncComponent(ArcanusComponents.SPELL_MEMORY);
                return Command.SINGLE_SUCCESS;
            }

            context.getSource().sendFailure(Arcanus.translate("commands", "spells.does_not_have", player.getDisplayName(), Component.translatable(spell.getTranslationKey(), Arcanus.SPELL.getKey(spell))));
            return 0;
        }

        public static int giveAllSpellBooks(ServerPlayer player) {
            Arcanus.SPELL.forEach(spell -> giveSpellBook(player, spell));
            return Arcanus.SPELL.size();
        }

        public static int giveSpellBook(ServerPlayer player, Spell spell) {
            ItemStack book = SpellBooks.getSpellBook(spell, player.getRandom());
            ArcanusHelper.giveOrDrop(player, book);
            return Command.SINGLE_SUCCESS;
        }
    }
}

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
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import java.util.Comparator;
import java.util.Set;

public class ArcanusCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(CommandManager.literal("spells")
                .then(CommandManager.literal("list").requires(source -> source.hasPermissionLevel(0))
                        .executes(context -> SpellsCommand.listPlayerSpells(context, context.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("player", EntityArgumentType.player()).requires(source -> source.hasPermissionLevel(3))
                                .executes(context -> SpellsCommand.listPlayerSpells(context, EntityArgumentType.getPlayer(context, "player")))))
                .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("all", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                                    return SpellsCommand.addAllSpellsToPlayer(ctx, player);
                                }))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                                    return SpellsCommand.addSpellToPlayer(ctx, player);
                                }))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("all", StringArgumentType.word())
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                            return SpellsCommand.addAllSpellsToPlayer(ctx, player);
                                        }))
                                .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
                                            return SpellsCommand.addSpellToPlayer(ctx, player);
                                        }))))
                .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(3))
                        .then(CommandManager.argument("all", StringArgumentType.word())
                                .executes(ctx -> SpellsCommand.removeAllSpellsFromPlayer(ctx, ctx.getSource().getPlayerOrThrow())))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(ctx -> SpellsCommand.removeSpellFromPlayer(ctx, ctx.getSource().getPlayerOrThrow()))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("all", StringArgumentType.word())
                                        .executes(ctx -> SpellsCommand.removeAllSpellsFromPlayer(ctx, EntityArgumentType.getPlayer(ctx, "player"))))
                                .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                        .executes(ctx -> SpellsCommand.removeSpellFromPlayer(ctx, EntityArgumentType.getPlayer(ctx, "player"))))))
                .then(CommandManager.literal("spellbook").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("all")
                                .executes(context -> SpellsCommand.giveAllSpellBooks(context.getSource().getPlayerOrThrow()))
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(EntityArgumentType.getPlayer(context, "player"), RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value())))
                        .then(CommandManager.argument("spell", RegistryEntryArgumentType.registryEntry(buildContext, Arcanus.SPELL.getKey()))
                                .executes(context -> SpellsCommand.giveSpellBook(context.getSource().getPlayerOrThrow(), RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value()))
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> SpellsCommand.giveSpellBook(EntityArgumentType.getPlayer(context, "player"), RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value())))))))));
    }

    private static class SpellsCommand {
        public static int listPlayerSpells(CommandContext<ServerCommandSource> context, PlayerEntity player) {
            Set<Spell> spells = player.getComponent(ArcanusComponents.SPELL_MEMORY).getKnownSpells();
            if (spells.isEmpty()) {
                context.getSource().sendError(Arcanus.translate("commands", "spells.no_known_spells", player.getDisplayName()));
                return 0;
            }

            MutableText knownSpells = Text.literal("");
            spells.stream().sorted(Comparator.comparing(spell -> Arcanus.SPELL.getId(spell).toString())).forEachOrdered(spell -> knownSpells.append("\n    - ").append(Text.translatable(spell.getTranslationKey())).append(" (" + spell.getSpellPattern().get(0).getSymbol() + "-" + spell.getSpellPattern().get(1).getSymbol() + "-" + spell.getSpellPattern().get(2).getSymbol() + ")"));

            context.getSource().sendFeedback(Arcanus.translate("commands", "spells.list", player.getEntityName(), knownSpells), false);
            return spells.size();
        }

        public static int addAllSpellsToPlayer(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            int count = (int) Arcanus.SPELL.stream().filter(memory::unlockSpell).count();
            player.syncComponent(ArcanusComponents.SPELL_MEMORY);
            context.getSource().sendFeedback(Arcanus.translate("commands", "spells.added_all", player.getDisplayName()), false);
            return Math.max(count, 1);
        }

        public static int addSpellToPlayer(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (memory.unlockSpell(spell)) {
                context.getSource().sendFeedback(Arcanus.translate("commands", "spells.added", Text.translatable(spell.getTranslationKey(), Arcanus.SPELL.getId(spell)), player.getEntityName()), false);
                player.syncComponent(ArcanusComponents.SPELL_MEMORY);
                return Command.SINGLE_SUCCESS;
            }

            context.getSource().sendError(Arcanus.translate("commands", "spells.already_known", player.getEntityName(), Text.translatable(spell.getTranslationKey(), Arcanus.SPELL.getId(spell))));
            return 0;
        }

        public static int removeAllSpellsFromPlayer(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            memory.clear();
            player.syncComponent(ArcanusComponents.SPELL_MEMORY);
            context.getSource().sendFeedback(Arcanus.translate("commands", "spells.cleared", player.getDisplayName()), false);
            return Command.SINGLE_SUCCESS;
        }

        public static int removeSpellFromPlayer(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) throws CommandSyntaxException {
            SpellMemory memory = player.getComponent(ArcanusComponents.SPELL_MEMORY);
            Spell spell = RegistryEntryArgumentType.getRegistryEntry(context, "spell", Arcanus.SPELL_KEY).value();

            if (memory.removeSpell(spell)) {
                context.getSource().sendFeedback(Arcanus.translate("commands", "spells.removed", Text.translatable(spell.getTranslationKey(), Arcanus.SPELL.getId(spell)), player.getDisplayName()), false);
                player.syncComponent(ArcanusComponents.SPELL_MEMORY);
                return Command.SINGLE_SUCCESS;
            }

            context.getSource().sendError(Arcanus.translate("commands", "spells.does_not_have", player.getDisplayName(), Text.translatable(spell.getTranslationKey(), Arcanus.SPELL.getId(spell))));
            return 0;
        }

        public static int giveAllSpellBooks(ServerPlayerEntity player) {
            Arcanus.SPELL.forEach(spell -> giveSpellBook(player, spell));
            return Arcanus.SPELL.size();
        }

        public static int giveSpellBook(ServerPlayerEntity player, Spell spell) {
            ItemStack book = SpellBooks.getSpellBook(spell, player.getRandom());
            ArcanusHelper.giveOrDrop(player, book);
            return Command.SINGLE_SUCCESS;
        }
    }
}

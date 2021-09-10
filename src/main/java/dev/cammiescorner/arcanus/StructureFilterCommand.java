package dev.cammiescorner.arcanus;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.Tag;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StructureFilterCommand {
	private static final Reloader RELOADER = new Reloader();
	private static final String NAMESPACE = Arcanus.MOD_ID;

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			var builder = CommandManager.literal(NAMESPACE);
			append(builder);
			dispatcher.register(builder);
		});
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(RELOADER);
	}

	private static void append(LiteralArgumentBuilder<ServerCommandSource> builder) {
		builder.then(CommandManager.literal("filter_structures").requires(src -> src.hasPermissionLevel(2)).then(CommandManager.literal("block").then(CommandManager.argument("block", BlockStateArgumentType.blockState()).executes(ctx -> {
			var stateArg = BlockStateArgumentType.getBlockState(ctx, "block");
			var blockToTestFor = stateArg.getBlockState().getBlock();
			var ret = execute(ctx, block -> block == blockToTestFor);
			if (ret == 0) {
				ctx.getSource().sendError(new LiteralText("no structures found"));
			}
			return ret;
		}))).then(CommandManager.literal("tag").then(CommandManager.argument("tag", BlockTagArgumentType.tag()).executes(ctx -> {
			var tag = BlockTagArgumentType.getTag(ctx, "tag");
			var ret = execute(ctx, tag::contains);
			if (ret == 0) {
				ctx.getSource().sendError(new LiteralText("no structures found"));
			}
			return ret;
		}))));
	}

	private static int execute(CommandContext<ServerCommandSource> ctx, Predicate<Block> filter) {
		var structureManager = ctx.getSource().getWorld().getStructureManager();
		AtomicInteger count = new AtomicInteger(0);
		RELOADER.getStructureNames().stream().filter(id -> structureManager.getStructure(id).map(structure -> {
			NbtCompound nbt = new NbtCompound();
			structure.writeNbt(nbt);
			NbtList palette = nbt.getList("palette", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < palette.size(); i++) {
				NbtCompound element = palette.getCompound(i);
				Identifier blockName = new Identifier(element.getString("Name"));
				if (Registry.BLOCK.getOrEmpty(blockName).filter(filter).isPresent()) {
					return true;
				}
			}
			return false;
		}).orElse(false)).sorted(Comparator.comparing(Identifier::toString)).forEach(id -> {
			var text = new LiteralText(id.toString()).styled(it -> {
				it.withFormatting(Formatting.WHITE);
				it.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id.toString()));
				it.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click")));
				return it;
			});
			ctx.getSource().sendFeedback(new LiteralText("#" + count.incrementAndGet() + ": ").formatted(Formatting.GRAY).append(text), false);
		});
		return count.get();
	}

	private static class Reloader implements SimpleSynchronousResourceReloadListener {

		private static final Identifier RELOADER_ID = new Identifier(NAMESPACE, "structure_finder");
		private static final String PREFIX = "structures";
		private static final int PREFIX_LENGTH = PREFIX.length() + 1;
		private static final String SUFFIX = ".nbt";
		private static final int SUFFIX_LENGTH = SUFFIX.length();
		private Set<Identifier> structureNames = Collections.emptySet();

		@Override
		public Identifier getFabricId() {
			return RELOADER_ID;
		}

		public Set<Identifier> getStructureNames() {
			return structureNames;
		}

		@Override
		public Collection<Identifier> getFabricDependencies() {
			return Collections.singleton(ResourceReloadListenerKeys.TAGS);
		}

		@Override
		public void reload(ResourceManager manager) {
			structureNames = manager.findResources(PREFIX, path -> path.endsWith(SUFFIX)).stream().map(path -> new Identifier(path.getNamespace(), path.getPath().substring(PREFIX_LENGTH, path.getPath().length() - SUFFIX_LENGTH))).collect(Collectors.toSet());
		}
	}

	public static class BlockTagArgumentType extends IdentifierArgumentType {

		//TODO add lang file entries
		private static final DynamicCommandExceptionType UNKNOWN_TAG = new DynamicCommandExceptionType(id -> new TranslatableText("block_tag.unknown", id));

		public static ArgumentType<Identifier> tag() {
			return IdentifierArgumentType.identifier();
		}

		public static Tag<Block> getTag(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
			Identifier id = IdentifierArgumentType.getIdentifier(ctx, name);
			var tag = ctx.getSource().getWorld().getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getTag(id);
			if (tag != null) {
				return tag;
			}
			throw UNKNOWN_TAG.create(id);
		}
	}
}

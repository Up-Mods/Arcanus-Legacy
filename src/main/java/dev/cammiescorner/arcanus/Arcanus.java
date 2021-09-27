package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.common.packets.CastSpellMessage;
import dev.cammiescorner.arcanus.common.screens.BookshelfScreenHandler;
import dev.cammiescorner.arcanus.common.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.common.structure.processor.LecternStructureProcessor;
import dev.cammiescorner.arcanus.core.integration.ArcanusConfig;
import dev.cammiescorner.arcanus.core.registry.*;
import dev.cammiescorner.arcanus.core.util.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Arcanus implements ModInitializer {
	//-----Custom Registries-----//
	public static final Registry<Spell> SPELL = createRegistry("spell", Spell.class);

	//-----Miscellaneous-----//
	public static ArcanusConfig config;
	public static final String MOD_ID = "arcanus";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "general")).appendItems(list -> {
		ArcanusHelper.addWandsToTab(list);
		Registry.ITEM.forEach(item -> item.appendStacks(Arcanus.ITEM_GROUP, (DefaultedList<ItemStack>) list));
		Arcanus.SPELL.forEach(spell -> list.add(SpellBooks.getSpellBook(spell)));
	}).icon(() -> new ItemStack(ModItems.MASTER_WAND)).build();

	//-----Loot Functions-----//
	public static final LootFunctionType ARCANUS_LOOT_FUNCTION = new LootFunctionType(new ArcanusLootFunction.Serializer());

	//-----Structure Processors-----//
	public static final StructureProcessorType<LecternStructureProcessor> LECTERN_PROCESSOR = StructureProcessorType.register("set_lectern_book", LecternStructureProcessor.CODEC);
	public static final StructureProcessorType<BookshelfReplacerStructureProcessor> BOOKSHELF_PROCESSOR = StructureProcessorType.register("replace_bookshelf", BookshelfReplacerStructureProcessor.CODEC);

	//-----Screen Handlers-----//
	public static final ScreenHandlerType<BookshelfScreenHandler> BOOKSHELF_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Arcanus.MOD_ID, "fillable_bookshelf"), BookshelfScreenHandler::new);

	@Override
	public void onInitialize() {
		DataTrackers.MANA.getId();
		AutoConfig.register(ArcanusConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ArcanusConfig.class).getConfig();

		ServerPlayNetworking.registerGlobalReceiver(CastSpellMessage.ID, CastSpellMessage::handle);
		Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier(Arcanus.MOD_ID, "arcanus_loot_function"), ARCANUS_LOOT_FUNCTION);
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "casting_multiplier"), EntityAttributes.MANA_COST);
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "mana_regen"), EntityAttributes.MANA_REGEN);
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "burnout_regen"), EntityAttributes.BURNOUT_REGEN);
		Registry.register(Registry.ATTRIBUTE, new Identifier(Arcanus.MOD_ID, "mana_lock"), EntityAttributes.MANA_LOCK);

		ModItems.register();
		ModBlocks.register();
		ModBlockEntities.register();
		ModSpells.register();
		ModEntities.register();
		ModParticles.register();
		ModSoundEvents.register();
		ModCommands.register();

		EventHandler.commonEvents();

		LOGGER.info("imagine people still looking for these :hahayes:");
	}

	@SuppressWarnings("unchecked")
	private static <T> Registry<T> createRegistry(String name, Class<?> clazz) {
		Registry<?> registry = FabricRegistryBuilder.createSimple(clazz, new Identifier(MOD_ID, name)).buildAndRegister();
		return (Registry<T>) registry;
	}

	public static MutableText getSpellInputs(List<Pattern> pattern, int index) {
		return index >= pattern.size() || pattern.get(index) == null ? new LiteralText("?").formatted(Formatting.GRAY, Formatting.UNDERLINE) : new LiteralText(pattern.get(index).getSymbol()).formatted(Formatting.GREEN);
	}

	public static class DataTrackers {
		public static final TrackedData<Integer> MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Integer> BURNOUT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
		public static final TrackedData<Boolean> SHOW_MANA = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

	public static class EntityAttributes {
		public static final EntityAttribute MANA_COST = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".mana_cost", 1D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute MANA_REGEN = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".mana_regen", 1D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute BURNOUT_REGEN = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".burnout_regen", 1D, 0D, 1024D).setTracked(true);
		public static final EntityAttribute MANA_LOCK = new ClampedEntityAttribute("attribute.name.generic." + Arcanus.MOD_ID + ".mana_lock", 0D, 0D, 20D).setTracked(true);
	}
}

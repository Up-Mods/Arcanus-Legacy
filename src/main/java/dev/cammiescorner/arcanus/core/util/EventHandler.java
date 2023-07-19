package dev.cammiescorner.arcanus.core.util;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.structure.processor.BookshelfReplacerStructureProcessor;
import dev.cammiescorner.arcanus.common.structure.processor.LecternStructureProcessor;
import dev.cammiescorner.arcanus.core.registry.ModCommands;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
	private static final Identifier RUINED_PORTAL_LOOT_TABLE = new Identifier("minecraft", "chests/ruined_portal");
	private static final Identifier STRONGHOLD_LIBRARY_LOOT_TABLE = new Identifier("minecraft", "chests/stronghold_library");

	public static void commonEvents() {
		//-----Server Starting Callback-----//
		ServerLifecycleEvents.SERVER_STARTING.register(server -> EventHandler.addStructureProcessors(server.getRegistryManager().get(Registry.STRUCTURE_POOL_KEY)));

		//-----Loot Table Callback-----//
		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if(Arcanus.getConfig().strongholdsHaveBooks && STRONGHOLD_LIBRARY_LOOT_TABLE.equals(id) && !FabricLoader.getInstance().isModLoaded("betterstrongholds")) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(4)).withCondition(RandomChanceLootCondition.builder(0.5F).build()).withEntry(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
				supplier.withPool(poolBuilder.build());
			}

			if(Arcanus.getConfig().ruinedPortalsHaveBooks && RUINED_PORTAL_LOOT_TABLE.equals(id)) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().rolls(ConstantLootNumberProvider.create(1)).withCondition(RandomChanceLootCondition.builder(0.1F).build()).withEntry(createItemEntry(new ItemStack(Items.WRITTEN_BOOK)).build());
				supplier.withPool(poolBuilder.build());
			}
		});

		//-----Copy Player Data Callback-----//
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			((MagicUser) newPlayer).setMana(((MagicUser) oldPlayer).getMana());
			((MagicUser) newPlayer).setBurnout(((MagicUser) oldPlayer).getBurnout());
			((MagicUser) oldPlayer).getKnownSpells().forEach(spell -> ((MagicUser) newPlayer).setKnownSpell(Arcanus.SPELL.getId(spell)));
		});

		//-----Command Callback-----//
		CommandRegistrationCallback.EVENT.register(ModCommands::init);
	}

	private static ItemEntry.Builder<?> createItemEntry(ItemStack stack) {
		ItemEntry.Builder<?> builder = ItemEntry.builder(stack.getItem());

		builder.apply(new ArcanusLootFunction.Builder());

		return builder;
	}

	public static void addStructureProcessors(Registry<StructurePool> templatePoolRegistry) {
		templatePoolRegistry.forEach(pool -> pool.elements.forEach(element -> {
			if(element instanceof SinglePoolElement singleElement && singleElement.location.left().isPresent()) {
				String currentElement = singleElement.location.left().get().toString();

				if(Arcanus.getConfig().structuresWithBookshelves.contains(currentElement) || Arcanus.getConfig().structuresWithLecterns.contains(currentElement)) {
					StructureProcessorList originalProcessorList = singleElement.processors.value();
					List<StructureProcessor> mutableProcessorList = new ArrayList<>(originalProcessorList.getList());

					if(Arcanus.getConfig().doLecternProcessor)
						mutableProcessorList.add(LecternStructureProcessor.INSTANCE);
					if(Arcanus.getConfig().doBookshelfProcessor)
						mutableProcessorList.add(BookshelfReplacerStructureProcessor.INSTANCE);

					StructureProcessorList newProcessorList = new StructureProcessorList(mutableProcessorList);

					singleElement.processors = RegistryEntry.of(newProcessorList);
				}
			}
		}));
	}
}

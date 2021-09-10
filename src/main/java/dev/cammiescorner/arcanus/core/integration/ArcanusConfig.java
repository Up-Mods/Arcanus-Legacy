package dev.cammiescorner.arcanus.core.integration;

import dev.cammiescorner.arcanus.Arcanus;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.List;

@Config(name = Arcanus.MOD_ID)
public class ArcanusConfig implements ConfigData {
	@Comment("Should the Lectern Structure Processor run?")
	public boolean doLecternProcessor = true;

	@Comment("Should the Bookshelf Structure Processor run?")
	public boolean doBookshelfProcessor = true;

	@Comment("Should Village Libraries have Spell Books spawn?")
	public boolean librariesHaveBooks = true;

	@Comment("Should Stronghold Libraries have Spell Books spawn?")
	public boolean strongholdsHaveBooks = true;

	@Comment("Should Ruined Portals have Spell Books spawn?")
	public boolean ruinedPortalsHaveBooks = true;

	@Comment("Whether or not burnout should be applied when using too" +
			"\n    much mana.")
	public boolean haveBurnout = true;

	@Comment("The colour of objects created by spells (i.e. the laser" +
			"\n    from Solar Strike and the wall from Arcane Wall).")
	public String magicColour = Integer.toString(0x7ecdfb, 16);

	@Comment("The time Mana takes to refill by 1 in ticks before modifiers.")
	public int baseManaCooldown = 20;

	@Comment("The time Burnout takes to reduce by 1 in ticks before modifiers.")
	public int baseBurnoutCooldown = 60;

	@Comment("The Mana cost for the Lunge spell.")
	public int lungeCastingCost = 5;

	@Comment("The Mana cost for the Dream Warp spell.")
	public int dreamWarpCastingCost = 15;

	@Comment("The Mana cost for the Magic Missile spell.")
	public int magicMissileCastingCost = 3;

	@Comment("The Mana cost for the Telekinetic Shock spell.")
	public int telekinesisCastingCost = 4;

	@Comment("The Mana cost for the Heal spell.")
	public int healCastingCost = 10;

	@Comment("The Mana cost for the Discombobulate spell.")
	public int discombobulateCastingCost = 10;

	@Comment("The Mana cost for the Solar Strike spell.")
	public int solarStrikeCastingCost = 20;

	@Comment("The Mana cost for the Arcane Barrier spell.")
	public int arcaneBarrierCastingCost = 4;

	@Comment("A list of structures that have Bookshelves to be replaced by" +
			"\n    Fillable Bookshelves from Arcanus.")
	public List<String> structuresWithBookshelves = List.of(
			"minecraft:village/desert/houses/desert_library_1",
			"minecraft:village/plains/houses/plains_library_1",
			"minecraft:village/plains/houses/plains_library_2",
			"minecraft:village/savanna/houses/savanna_library_1",
			"minecraft:village/snowy/houses/snowy_library_1",
			"minecraft:village/taiga/houses/taiga_library_1",
			"minecraft:village/taiga/houses/taiga_medium_house_3",
			"minecraft:village/taiga/zombie/houses/taiga_library_1",
			"minecraft:village/taiga/zombie/houses/taiga_medium_house_3",
			"minecraft:woodland_mansion/1x1_b4",
			"minecraft:woodland_mansion/1x2_c4",
			"minecraft:woodland_mansion/1x2_d1",
			"minecraft:woodland_mansion/2x2_b3",
			"minecraft:woodland_mansion/2x2_b4",
			"betterstrongholds:rooms/cmd_yung",
			"betterstrongholds:rooms/grand_library",
			"betterstrongholds:rooms/library_md",
			"dungeons_arise:bandit_towers/bandit_towers_crossing_2",
			"dungeons_arise:bandit_towers/bandit_towers_crossing_5",
			"dungeons_arise:bandit_towers/bandit_towers_part_0",
			"dungeons_arise:bandit_towers/bandit_towers_part_1",
			"dungeons_arise:bandit_towers/bandit_towers_small_room_2",
			"dungeons_arise:bandit_village/bandit_village_deco_12",
			"dungeons_arise:bandit_village/bandit_village_house_1",
			"dungeons_arise:bandit_village/bandit_village_house_2",
			"dungeons_arise:heavenly_challenger/heavenly_challenger_part_2",
			"dungeons_arise:heavenly_challenger/heavenly_challenger_part_3",
			"dungeons_arise:illager_campsite/illager_campsite_tent_1",
			"dungeons_arise:illager_fort/illager_fort_part_0",
			"dungeons_arise:illager_fort/illager_fort_room_0",
			"dungeons_arise:illager_fort/illager_fort_room_2",
			"dungeons_arise:illager_fort/illager_fort_room_8",
			"dungeons_arise:illager_hall/illager_hall_tower_main_part_0",
			"dungeons_arise:illager_hall/illager_hall_tower_part_0",
			"dungeons_arise:infested_temple/infested_temple_level_0",
			"dungeons_arise:infested_temple/infested_temple_level_1",
			"dungeons_arise:infested_temple/infested_temple_level_2",
			"dungeons_arise:infested_temple/infested_temple_room_0",
			"dungeons_arise:infested_temple/infested_temple_room_1",
			"dungeons_arise:infested_temple/infested_temple_room_3",
			"dungeons_arise:infested_temple/infested_temple_room_4",
			"dungeons_arise:infested_temple/infested_temple_room_7",
			"dungeons_arise:infested_temple/infested_temple_room_8",
			"dungeons_arise:lighthouse/lighthouse_part_1",
			"dungeons_arise:monastery/monastery_room_0",
			"dungeons_arise:monastery/monastery_top_0",
			"dungeons_arise:mushroom_house/mushroom_house_bottom_1",
			"dungeons_arise:mushroom_village/mushroom_village_house_13",
			"dungeons_arise:plague_asylum/plague_asylum_cell_normal_6",
			"dungeons_arise:plague_asylum/plague_asylum_cell_security_1",
			"dungeons_arise:plague_asylum/plague_asylum_cell_security_2",
			"dungeons_arise:plague_asylum/plague_asylum_cell_security_3",
			"dungeons_arise:plague_asylum/plague_asylum_cell_security_4",
			"dungeons_arise:scorched_mines/scorched_mines_housing_0",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_10",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_12",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_13",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_14",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_16",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_17",
			"dungeons_arise:shiraz_palace/shiraz_palace_part_9",
			"dungeons_arise:small_prairie_house/small_prairie_house_clean_0_top",
			"dungeons_arise:thornborn_towers/thornborn_towers_hanging_big_0_main",
			"dungeons_arise:thornborn_towers/thornborn_towers_hanging_medium_1",
			"dungeons_arise:thornborn_towers/thornborn_towers_hanging_medium_2",
			"dungeons_arise:thornborn_towers/thornborn_towers_part_0_main",
			"dungeons_arise:thornborn_towers/thornborn_towers_room_0",
			"dungeons_arise:thornborn_towers/thornborn_towers_room_1",
			"dungeons_arise:thornborn_towers/thornborn_towers_room_2",
			"dungeons_arise:thornborn_towers/thornborn_towers_room_3",
			"dungeons_arise:undead_pirate_ship/undead_pirate_ship_part_0",
			"mostructures:market/main",
			"mostructures:pyramid/base",
			"mostructures:ship/bottom_lower",
			"mostructures:tavern/tavern_1",
			"mostructures:tavern/tavern_2",
			"repurposed_structures:mansions/birch/1x1_b4",
			"repurposed_structures:mansions/birch/1x2_c4",
			"repurposed_structures:mansions/birch/1x2_d1",
			"repurposed_structures:mansions/birch/2x2_b3",
			"repurposed_structures:mansions/birch/2x2_b4",
			"repurposed_structures:mansions/desert/1x1_b4",
			"repurposed_structures:mansions/desert/1x2_c4",
			"repurposed_structures:mansions/desert/1x2_d1",
			"repurposed_structures:mansions/desert/2x2_b3",
			"repurposed_structures:mansions/desert/2x2_b4",
			"repurposed_structures:mansions/jungle/1x1_b4",
			"repurposed_structures:mansions/jungle/1x2_c4",
			"repurposed_structures:mansions/jungle/1x2_d1",
			"repurposed_structures:mansions/jungle/2x2_b3",
			"repurposed_structures:mansions/jungle/2x2_b4",
			"repurposed_structures:mansions/oak/1x1_b4",
			"repurposed_structures:mansions/oak/1x2_c4",
			"repurposed_structures:mansions/oak/1x2_d1",
			"repurposed_structures:mansions/oak/2x2_b3",
			"repurposed_structures:mansions/oak/2x2_b4",
			"repurposed_structures:mansions/savanna/1x1_b4",
			"repurposed_structures:mansions/savanna/1x2_c4",
			"repurposed_structures:mansions/savanna/1x2_d1",
			"repurposed_structures:mansions/savanna/2x2_b3",
			"repurposed_structures:mansions/savanna/2x2_b4",
			"repurposed_structures:mansions/snowy/1x1_b4",
			"repurposed_structures:mansions/snowy/1x2_c4",
			"repurposed_structures:mansions/snowy/1x2_d1",
			"repurposed_structures:mansions/snowy/2x2_b3",
			"repurposed_structures:mansions/snowy/2x2_b4",
			"repurposed_structures:mansions/taiga/1x1_b4",
			"repurposed_structures:mansions/taiga/1x2_c4",
			"repurposed_structures:mansions/taiga/1x2_d1",
			"repurposed_structures:mansions/taiga/2x2_b3",
			"repurposed_structures:mansions/taiga/2x2_b4",
			"repurposed_structures:villages/badlands/houses/library_1",
			"repurposed_structures:villages/badlands/houses/library_2",
			"repurposed_structures:villages/badlands/houses/medium_house_1",
			"repurposed_structures:villages/badlands/houses/medium_house_3",
			"repurposed_structures:villages/badlands/zombie/houses/medium_house_1",
			"repurposed_structures:villages/badlands/zombie/houses/medium_house_3",
			"repurposed_structures:villages/birch/houses/library_1",
			"repurposed_structures:villages/birch/houses/library_2",
			"repurposed_structures:villages/crimson/houses/library_1",
			"repurposed_structures:villages/crimson/houses/library_2",
			"repurposed_structures:villages/dark_forest/houses/library_1",
			"repurposed_structures:villages/dark_forest/houses/library_2",
			"repurposed_structures:villages/giant_taiga/houses/library_1",
			"repurposed_structures:villages/jungle/houses/library_1",
			"repurposed_structures:villages/mountains/houses/library_1",
			"repurposed_structures:villages/mountains/houses/medium_house_3",
			"repurposed_structures:villages/mountains/zombie/houses/library_1",
			"repurposed_structures:villages/mountains/zombie/houses/medium_house_3",
			"repurposed_structures:villages/oak/houses/library_1",
			"repurposed_structures:villages/oak/houses/library_2",
			"repurposed_structures:villages/swamp/houses/library_1",
			"repurposed_structures:villages/warped/houses/library_1",
			"repurposed_structures:villages/warped/houses/library_2",
			"stoneholm:abandoned_poi/library_01",
			"stoneholm:abandoned_poi/library_02",
			"stoneholm:poi/copper_shop",
			"stoneholm:poi/library_01",
			"stoneholm:poi/library_02",
			"stoneholm:poi/treasures/forbidden_knowledge",
			"stoneholm:wallpaper/wallpaper_03",
			"stonevaults:dungeon/rooms/library",
			"stonevaults:igloo/pieces/snowy_shelves",
			"stonevaults:magetower/floors/library",
			"stonevaults:magetower/sidetower/bedroom",
			"stonevaults:pillager_dungeon/rooms/big_library",
			"stonevaults:pillager_dungeon/rooms/library"
	);

	@Comment("A list of structures that have Lecterns to be filled with" +
			"\n    Spell Books from Arcanus.")
	public List<String> structuresWithLecterns = List.of(
			"minecraft:village/desert/houses/desert_library_1",
			"minecraft:village/plains/houses/plains_library_1",
			"minecraft:village/plains/houses/plains_library_2",
			"minecraft:village/savanna/houses/savanna_library_1",
			"minecraft:village/snowy/houses/snowy_library_1",
			"minecraft:village/taiga/houses/taiga_library_1",
			"minecraft:village/taiga/zombie/houses/taiga_library_1",
			"betterstrongholds:rooms/grand_library",
			"betterstrongholds:rooms/library_md",
			"betterstrongholds:rooms/treasure_room_lg",
			"dungeons_arise:monastery/monastery_top_0",
			"mostructures:factory/base",
			"mostructures:market/main",
			"mostructures:ship/bottom_lower",
			"repurposed_structures:villages/badlands/houses/library_1",
			"repurposed_structures:villages/badlands/houses/library_2",
			"repurposed_structures:villages/birch/houses/library_1",
			"repurposed_structures:villages/birch/houses/library_2",
			"repurposed_structures:villages/crimson/houses/library_1",
			"repurposed_structures:villages/crimson/houses/library_2",
			"repurposed_structures:villages/dark_forest/houses/library_1",
			"repurposed_structures:villages/dark_forest/houses/library_2",
			"repurposed_structures:villages/giant_taiga/houses/library_1",
			"repurposed_structures:villages/jungle/houses/library_1",
			"repurposed_structures:villages/mountains/houses/library_1",
			"repurposed_structures:villages/mountains/zombie/houses/library_1",
			"repurposed_structures:villages/oak/houses/library_1",
			"repurposed_structures:villages/oak/houses/library_2",
			"repurposed_structures:villages/swamp/houses/library_1",
			"repurposed_structures:villages/warped/houses/library_1",
			"repurposed_structures:villages/warped/houses/library_2",
			"stoneholm:abandoned_poi/library_01",
			"stoneholm:abandoned_poi/library_02",
			"stoneholm:poi/library_01",
			"stoneholm:poi/library_02",
			"stonevaults:magetower/floors/library",
			"stonevaults:pillager_dungeon/rooms/big_library",
			"stonevaults:pillager_dungeon/rooms/storage_2"
	);
}

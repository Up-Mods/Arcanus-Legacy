package dev.cammiescorner.arcanus.core.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.common.blocks.FillableBookshelfBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModBlocks {
	//-----Block Map-----//
	public static final LinkedHashMap<Block, Identifier> BLOCKS = new LinkedHashMap<>();

	//-----Blocks-----//
	public static final Block BOOKSHELF = create("fillable_bookshelf", new FillableBookshelfBlock(AbstractBlock.Settings.copy(Blocks.BOOKSHELF)));

	//-----Registry-----//
	public static void register() {
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
		BLOCKS.keySet().forEach(block -> Registry.register(Registry.ITEM, BLOCKS.get(block), getItem(block)));
	}

	private static BlockItem getItem(Block block) {
		return new BlockItem(block, new Item.Settings().group(Arcanus.ITEM_GROUP));
	}

	private static <T extends Block> T create(String name, T block) {
		BLOCKS.put(block, new Identifier(Arcanus.MOD_ID, name));
		return block;
	}
}

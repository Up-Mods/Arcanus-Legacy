package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ArcanusTags {
	public static final TagKey<Block> ALTAR_PALETTE = TagKey.of(Registry.BLOCK_KEY, Arcanus.id("altar_palette"));
}

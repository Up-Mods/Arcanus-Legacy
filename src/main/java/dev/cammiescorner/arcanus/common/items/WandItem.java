package dev.cammiescorner.arcanus.common.items;

import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class WandItem extends Item {
	private final float castingMultiplier;
	private final int maxExp;
	private final Supplier<Item> upgrade;

	public WandItem(float castingMultiplier, int maxExp, @Nullable Supplier<Item> upgrade) {
		super(new FabricItemSettings().maxCount(1));
		this.castingMultiplier = castingMultiplier;
		this.maxExp = maxExp;
		this.upgrade = upgrade;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new LiteralText(stack.getOrCreateSubNbt(Arcanus.MOD_ID).getInt("Exp") + "/" + getMaxExp())
				.append(" Exp").formatted(Formatting.DARK_AQUA));

		if(getCastingMultiplier() != 1F) {
			String formattedMultiplier = String.format("%.0f", (getCastingMultiplier() * 100) - 100);

			tooltip.add(new LiteralText(getCastingMultiplier() > 1F ? "+" + formattedMultiplier : formattedMultiplier)
					.append("% ").append(new TranslatableText("wand." + Arcanus.MOD_ID + ".casting_cost"))
					.formatted(getCastingMultiplier() > 1F ? Formatting.RED : Formatting.GREEN));
		}
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 100;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return false;
	}

	public float getCastingMultiplier() {
		return castingMultiplier;
	}

	public int getMaxExp() {
		return maxExp;
	}

	public Item getUpgrade() {
		return upgrade.get();
	}

	public boolean hasUpgrade() {
		return upgrade != null;
	}
}

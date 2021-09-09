package dev.cammiescorner.arcanus.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.Arcanus;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class WandItem extends Item {
	private final int maxExp;
	private final Supplier<Item> upgrade;
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
	private static final UUID MANA_COST = UUID.fromString("41d46dfa-5776-4839-91d6-d8403ef35a00");

	public WandItem(float castingMultiplier, int maxExp, @Nullable Supplier<Item> upgrade) {
		super(new FabricItemSettings().maxCount(1));
		this.maxExp = maxExp;
		this.upgrade = upgrade;
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Arcanus.EntityAttributes.MANA_COST, new EntityAttributeModifier(MANA_COST, "Mana Cost", castingMultiplier, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
		this.attributeModifiers = builder.build();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new LiteralText(stack.getOrCreateSubNbt(Arcanus.MOD_ID).getInt("Exp") + "/" + getMaxExp())
				.append(" Exp").formatted(Formatting.DARK_AQUA));
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		return 100;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return false;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
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

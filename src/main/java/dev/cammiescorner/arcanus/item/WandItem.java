package dev.cammiescorner.arcanus.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class WandItem extends Item {
    private static final UUID MANA_COST = UUID.fromString("41d46dfa-5776-4839-91d6-d8403ef35a00");
    private final int maxExp;
    @Nullable
    private Supplier<Item> upgrade;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public WandItem(float castingMultiplier, int maxExp, @Nullable Supplier<Item> upgrade, Item.Properties settings) {
        super(settings.stacksTo(1));
        this.maxExp = maxExp;
        this.upgrade = upgrade;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ArcanusEntityAttributes.MANA_COST.get(), new AttributeModifier(MANA_COST, "Mana Cost", castingMultiplier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        this.attributeModifiers = builder.build();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        CompoundTag nbt = stack.getTagElement(Arcanus.MODID);
        int xp = nbt != null ? nbt.getInt("Exp") : 0;
        tooltip.add(Component.literal(xp + "/" + getMaxExp()).append(" Exp").withStyle(ChatFormatting.DARK_AQUA));
        if (hasUpgrade()) {
            tooltip.add(Arcanus.translate("tooltip", "wand_upgrade").withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 100;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public int getMaxExp() {
        return maxExp;
    }

    public Item getUpgrade() {
        return upgrade != null ? upgrade.get() : null;
    }

    public void setUpgrade(Supplier<Item> upgrade) {
        this.upgrade = upgrade;
    }

    public boolean hasUpgrade() {
        return upgrade != null;
    }
}

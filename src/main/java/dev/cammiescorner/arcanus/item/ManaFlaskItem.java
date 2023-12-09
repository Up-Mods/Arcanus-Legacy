package dev.cammiescorner.arcanus.item;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaFlaskItem extends Item {

    public ManaFlaskItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide() && user instanceof Player player) {
            MagicCaster caster = player.getComponent(ArcanusComponents.MAGIC_CASTER);
            CompoundTag tag = stack.getOrCreateTagElement(Arcanus.MODID);

            if (player.isShiftKeyDown() && tag.getInt("Mana") < 4 && caster.getMana() >= 5) {
                if (!player.isCreative()) {
                    caster.drainMana(5);
                    caster.setLastCastTime(world.getGameTime());
                }

                tag.putInt("Mana", tag.getInt("Mana") + 1);
            } else if (tag.getInt("Mana") > 0) {
                if (!player.isCreative()) {
                    caster.addMana(5);
                    caster.setLastCastTime(world.getGameTime());
                }
                tag.putInt("Mana", tag.getInt("Mana") - 1);
                tag.putBoolean("Filling", false);

                user.gameEvent(GameEvent.DRINK);
            }

            user.syncComponent(ArcanusComponents.MAGIC_CASTER);
        }

        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        CompoundTag tag = user.getItemInHand(hand).getOrCreateTagElement(Arcanus.MODID);
        MagicCaster caster = user.getComponent(ArcanusComponents.MAGIC_CASTER);

        tag.putBoolean("Filling", user.isShiftKeyDown() && tag.getInt("Mana") < 4 && caster.getMana() >= 5);

        if (tag.getBoolean("Filling") || (caster.getMana() < caster.getMaxMana() && tag.getInt("Mana") > 0)) {
            return ItemUtils.startUsingInstantly(world, user, hand);
        } else {
            return InteractionResultHolder.fail(user.getItemInHand(hand));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int mana = stack.getOrCreateTagElement(Arcanus.MODID).getInt("Mana");
        return mana > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int mana = stack.getOrCreateTagElement(Arcanus.MODID).getInt("Mana");
        return Math.round((mana * 13F) / 4F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x3BB3CB;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Arcanus.MODID);
        return tag == null || tag.getBoolean("Filling") ? UseAnim.BOW : UseAnim.DRINK;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(Arcanus.MODID);

        if (tag == null || tag.getInt("Mana") <= 0) {
            return Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this)) + "_empty";
        }

        return super.getDescriptionId(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(Component.translatable(stack.getDescriptionId() + ".description").withStyle(ChatFormatting.DARK_AQUA));
    }
}

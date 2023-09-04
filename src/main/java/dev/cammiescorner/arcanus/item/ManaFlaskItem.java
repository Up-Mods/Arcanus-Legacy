package dev.cammiescorner.arcanus.item;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaFlaskItem extends Item {
    public ManaFlaskItem() {
        super(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient() && user instanceof PlayerEntity player) {
            MagicCaster caster = player.getComponent(ArcanusComponents.MAGIC_CASTER);
            NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);

            if (player.isSneaking() && tag.getInt("Mana") < 4 && caster.getMana() >= 5) {
                if (!player.isCreative()) {
                    caster.drainMana(5);
                    caster.setLastCastTime(world.getTime());
                }

                tag.putInt("Mana", tag.getInt("Mana") + 1);
            } else if (tag.getInt("Mana") > 0) {
                if (!player.isCreative()) {
                    caster.addMana(5);
                    caster.setLastCastTime(world.getTime());
                }
                tag.putInt("Mana", tag.getInt("Mana") - 1);
                tag.putBoolean("Filling", false);

                user.emitGameEvent(GameEvent.DRINK);
            }

            user.syncComponent(ArcanusComponents.MAGIC_CASTER);
        }

        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NbtCompound tag = user.getStackInHand(hand).getOrCreateSubNbt(Arcanus.MOD_ID);
        MagicCaster caster = user.getComponent(ArcanusComponents.MAGIC_CASTER);

        tag.putBoolean("Filling", user.isSneaking() && tag.getInt("Mana") < 4 && caster.getMana() >= 5);

        if (tag.getBoolean("Filling") || (caster.getMana() < caster.getMaxMana() && tag.getInt("Mana") > 0)) {
            return ItemUsage.consumeHeldItem(world, user, hand);
        } else {
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int mana = stack.getOrCreateSubNbt(Arcanus.MOD_ID).getInt("Mana");
        return mana > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int mana = stack.getOrCreateSubNbt(Arcanus.MOD_ID).getInt("Mana");
        return Math.round((mana * 13F) / 4F);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x3BB3CB;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);
        return tag == null || tag.getBoolean("Filling") ? UseAction.BOW : UseAction.DRINK;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

        if (tag == null || tag.getInt("Mana") <= 0) {
            return Util.createTranslationKey("item", Registries.ITEM.getId(this)) + "_empty";
        }

        return super.getTranslationKey(stack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable(stack.getTranslationKey() + ".description").formatted(Formatting.DARK_AQUA));
    }
}

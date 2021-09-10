package dev.cammiescorner.arcanus.common.items;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ManaFlaskItem extends Item {
	public ManaFlaskItem() {
		super(new FabricItemSettings().group(Arcanus.ITEM_GROUP).maxCount(1));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(!world.isClient() && user instanceof PlayerEntity player && player instanceof MagicUser magicUser) {
			NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);

			if(player.isSneaking() && tag.getInt("Mana") < 4 && magicUser.getMana() >= 5) {
				if(!player.isCreative()) {
					magicUser.addMana(-5);
					magicUser.setLastCastTime(world.getTime());
				}

				tag.putInt("Mana", tag.getInt("Mana") + 1);
			}
			else if(tag.getInt("Mana") > 0){
				if(!player.isCreative()) {
					magicUser.addMana(MathHelper.clamp((magicUser.getMaxMana() - magicUser.getBurnout()) - magicUser.getMana(), 0, 5));
					magicUser.setLastCastTime(world.getTime());
				}

				tag.putInt("Mana", tag.getInt("Mana") - 1);
				tag.putBoolean("Filling", false);
			}
		}

		return stack;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		NbtCompound tag = user.getStackInHand(hand).getOrCreateSubNbt(Arcanus.MOD_ID);
		MagicUser magicUser = (MagicUser) user;

		tag.putBoolean("Filling", user.isSneaking() && tag.getInt("Mana") < 4 && magicUser.getMana() >= 5);

		if(tag.getBoolean("Filling") || (magicUser.getMana() < magicUser.getMaxMana() && tag.getInt("Mana") > 0))
			return ItemUsage.consumeHeldItem(world, user, hand);
		else
			return TypedActionResult.fail(user.getStackInHand(hand));
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if(isIn(group)) {
			ItemStack stack = new ItemStack(this);
			NbtCompound tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
			tag.putInt("Mana", 0);
			stacks.add(stack);

			stack = new ItemStack(this);
			tag = stack.getOrCreateSubNbt(Arcanus.MOD_ID);
			tag.putInt("Mana", 4);
			stacks.add(stack);
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
		return tag.getBoolean("Filling") ? UseAction.BOW : UseAction.DRINK;
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

		if(tag == null || tag.getInt("Mana") <= 0)
			return Util.createTranslationKey("item", Registry.ITEM.getId(this)) + "_empty";

		return super.getTranslationKey(stack);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return PotionItem.field_30888;
	}
}

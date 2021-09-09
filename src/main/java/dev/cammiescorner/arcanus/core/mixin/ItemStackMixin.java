package dev.cammiescorner.arcanus.core.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static dev.cammiescorner.arcanus.Arcanus.EntityAttributes.*;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Unique private final List<EntityAttribute> inverseAttributes = List.of(MANA_COST, MANA_REGEN, BURNOUT_REGEN, MANA_LOCK);

	@Inject(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 8), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void changePositiveFormatting(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, int i, EquipmentSlot[] slots, int j, int k, EquipmentSlot slot, Multimap<EntityAttribute, EntityAttributeModifier> map, Iterator<EntityAttributeModifier> iterator, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier modifier, double d, double g) {
		if(inverseAttributes.contains(entry.getKey()))
			((MutableText) list.get(list.size() - 1)).formatted(Formatting.RED);
	}

	@Inject(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 9), locals = LocalCapture.CAPTURE_FAILSOFT)
	public void changeNegativeFormatting(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list, int i, EquipmentSlot[] slots, int j, int k, EquipmentSlot slot, Multimap<EntityAttribute, EntityAttributeModifier> map, Iterator<EntityAttributeModifier> iterator, Map.Entry<EntityAttribute, EntityAttributeModifier> entry, EntityAttributeModifier modifier, double d, double g) {
		if(inverseAttributes.contains(entry.getKey()))
			((MutableText) list.get(list.size() - 1)).formatted(Formatting.BLUE);
	}
}

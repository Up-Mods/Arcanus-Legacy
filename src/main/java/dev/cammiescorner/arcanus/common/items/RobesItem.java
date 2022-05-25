package dev.cammiescorner.arcanus.common.items;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.List;

public abstract class RobesItem extends ArmorItem {
	public RobesItem(ArmorMaterial material, EquipmentSlot slot) {
		super(material, slot, new QuiltItemSettings().group(Arcanus.ITEM_GROUP));
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		if(Arcanus.HOOD_ITEMS.contains(this)) {
			NbtCompound tag = stack.getSubNbt(Arcanus.MOD_ID);

			if(tag != null && tag.getBoolean("Closed"))
				tooltip.add(new TranslatableText(Arcanus.MOD_ID + ".mage_robes.closed").formatted(Formatting.GRAY));
			else
				tooltip.add(new TranslatableText(Arcanus.MOD_ID + ".mage_robes.open").formatted(Formatting.GRAY));
		}
	}
}

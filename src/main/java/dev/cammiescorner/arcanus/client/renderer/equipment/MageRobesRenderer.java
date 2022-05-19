package dev.cammiescorner.arcanus.client.renderer.equipment;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.models.equipment.MageRobesModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class MageRobesRenderer implements ArmorRenderer {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static MageRobesModel<LivingEntity> armorModel;
	private final Identifier texture;

	public MageRobesRenderer(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> entityModel) {
		if(armorModel == null)
			armorModel = new MageRobesModel<>(client.getEntityModelLoader().getModelPart(MageRobesModel.MODEL_LAYER));

		NbtCompound nbt = stack.getSubNbt(Arcanus.MOD_ID);
		boolean isClosed = nbt != null && nbt.getBoolean("Closed");
		entityModel.setAttributes(armorModel);
		armorModel.setVisible(true);
		armorModel.closedHood.visible = slot == EquipmentSlot.HEAD && isClosed;
		armorModel.openHood.visible = slot == EquipmentSlot.HEAD && !isClosed;
		armorModel.cloak.visible = slot == EquipmentSlot.HEAD;
		armorModel.garb.visible = slot == EquipmentSlot.CHEST;
		armorModel.leftSleeve.visible = slot == EquipmentSlot.CHEST;
		armorModel.rightSleeve.visible = slot == EquipmentSlot.CHEST;
		armorModel.belt.visible = slot == EquipmentSlot.LEGS;
		armorModel.leftShoe.visible = slot == EquipmentSlot.FEET;
		armorModel.rightShoe.visible = slot == EquipmentSlot.FEET;
		ArmorRenderer.renderPart(matrices, vertices, light, stack, armorModel, texture);

		ModelPart cape = armorModel.cloak.getChild("cube_r2");
		// TODO animate cape on the cloak
	}
}

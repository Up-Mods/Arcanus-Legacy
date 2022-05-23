package dev.cammiescorner.arcanus.client.models.equipment;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class TimeCultRobesModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Arcanus.id("time_cultist_robes"), "main");
	public final ModelPart closedHood;
	public final ModelPart cloak;
	public final ModelPart openHood;
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart garb;
	public final ModelPart leftLegSleeve;
	public final ModelPart rightLegSleeve;
	public final ModelPart leftShoe;
	public final ModelPart rightShoe;

	public TimeCultRobesModel(ModelPart root) {
		super(root, RenderLayer::getArmorCutoutNoCull);
		closedHood = head.getChild("closedHood");
		cloak = body.getChild("cloak");
		openHood = cloak.getChild("openHood");
		leftSleeve = leftArm.getChild("leftSleeve");
		rightSleeve = rightArm.getChild("rightSleeve");
		garb = body.getChild("garb");
		leftLegSleeve = leftLeg.getChild("leftLegSleeve");
		rightLegSleeve = rightLeg.getChild("rightLegSleeve");
		leftShoe = leftLeg.getChild("leftShoe");
		rightShoe = rightLeg.getChild("rightShoe");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData head = data.getRoot().getChild(EntityModelPartNames.HEAD);
		ModelPartData body = data.getRoot().getChild(EntityModelPartNames.BODY);
		ModelPartData rightArm = data.getRoot().getChild(EntityModelPartNames.RIGHT_ARM);
		ModelPartData leftArm = data.getRoot().getChild(EntityModelPartNames.LEFT_ARM);
		ModelPartData rightLeg = data.getRoot().getChild(EntityModelPartNames.RIGHT_LEG);
		ModelPartData leftLeg = data.getRoot().getChild(EntityModelPartNames.LEFT_LEG);

		ModelPartData closedHood = head.addChild("closedHood", ModelPartBuilder.create().uv(0, 64).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r1 = closedHood.addChild("cube_r1", ModelPartBuilder.create().uv(24, 80).cuboid(3.6F, -6.0F, -3.5F, 3.0F, 7.0F, 7.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));
		ModelPartData cube_r2 = closedHood.addChild("cube_r2", ModelPartBuilder.create().uv(17, 94).cuboid(-6.6F, -6.0F, -3.5F, 3.0F, 7.0F, 7.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
		ModelPartData cloak = body.addChild("cloak", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r3 = cloak.addChild("cube_r3", ModelPartBuilder.create().uv(15, 108).cuboid(-4.0F, -1.0F, -1.0F, 8.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 3.0F, 0.2182F, 0.0F, 0.0F));
		ModelPartData openHood = cloak.addChild("openHood", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r4 = openHood.addChild("cube_r4", ModelPartBuilder.create().uv(30, 117).cuboid(-5.0F, -4.0F, -1.0F, 10.0F, 4.0F, 7.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		ModelPartData garb = body.addChild("garb", ModelPartBuilder.create().uv(45, 105).cuboid(-3.0F, -3.0F, -2.0F, 6.0F, 3.0F, 1.0F, new Dilation(0.4F)).uv(0, 80).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 11.0F, 4.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));
		ModelPartData cube_r5 = garb.addChild("cube_r5", ModelPartBuilder.create().uv(0, 120).cuboid(-2.0F, 0.5F, -4.0F, 4.0F, 4.0F, 4.0F, new Dilation(-0.5F)), ModelTransform.of(0.0F, -12.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		ModelPartData backCover = garb.addChild("backCover", ModelPartBuilder.create().uv(37, 78).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 8.0F, 1.0F, new Dilation(0.349F)), ModelTransform.of(0.0F, -1.0F, 1.0F, 0.2618F, 0.0F, 0.0F));
		ModelPartData rightCover = garb.addChild("rightCover", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r6 = rightCover.addChild("cube_r6", ModelPartBuilder.create().uv(51, 83).cuboid(-1.2F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.349F)), ModelTransform.of(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
		ModelPartData leftCover = garb.addChild("leftCover", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r7 = leftCover.addChild("cube_r7", ModelPartBuilder.create().uv(16, 115).cuboid(0.2F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.349F)), ModelTransform.of(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
		ModelPartData rightSleeve = rightArm.addChild("rightSleeve", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r8 = rightSleeve.addChild("cube_r8", ModelPartBuilder.create().uv(32, 64).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.29F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0524F));
		ModelPartData leftSleeve = leftArm.addChild("leftSleeve", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 0.0F, 0.0F));
		ModelPartData cube_r9 = leftSleeve.addChild("cube_r9", ModelPartBuilder.create().uv(0, 95).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.29F)), ModelTransform.of(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0524F));
		ModelPartData rightShoe = rightLeg.addChild("rightShoe", ModelPartBuilder.create().uv(48, 64).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.4F)), ModelTransform.pivot(-0.2F, 0.0F, 0.0F));
		ModelPartData rightLegSleeve = rightLeg.addChild("rightLegSleeve", ModelPartBuilder.create().uv(37, 94).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.299F)), ModelTransform.pivot(-0.1F, 0.0F, 0.0F));
		ModelPartData leftShoe = leftLeg.addChild("leftShoe", ModelPartBuilder.create().uv(0, 111).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.399F)), ModelTransform.pivot(0.2F, 0.0F, 0.0F));
		ModelPartData leftLegSleeve = leftLeg.addChild("leftLegSleeve", ModelPartBuilder.create().uv(33, 105).cuboid(-1.9F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(data, 64, 128);
	}
}

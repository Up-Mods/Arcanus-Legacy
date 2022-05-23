package dev.cammiescorner.arcanus.client.models.equipment;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class MageRobesModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Arcanus.id("mage_robes"), "main");
	public final ModelPart closedHood;
	public final ModelPart cloak;
	public final ModelPart openHood;
	public final ModelPart leftSleeve;
	public final ModelPart rightSleeve;
	public final ModelPart belt;
	public final ModelPart garb;
	public final ModelPart leftShoe;
	public final ModelPart rightShoe;

	public MageRobesModel(ModelPart root) {
		super(root, RenderLayer::getArmorCutoutNoCull);
		closedHood = head.getChild("closedHood");
		cloak = body.getChild("cloak");
		openHood = cloak.getChild("openHood");
		leftSleeve = leftArm.getChild("leftSleeve");
		rightSleeve = rightArm.getChild("rightSleeve");
		garb = body.getChild("garb");
		belt = body.getChild("belt");
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

		ModelPartData closedHood = head.addChild("closedHood", ModelPartBuilder.create().uv(0, 64).cuboid(-4F, -8F, -4F, 8F, 8F, 8F, new Dilation(0.5F)), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r1 = closedHood.addChild("cube_r1", ModelPartBuilder.create().uv(29, 78).cuboid(-3F, 0.2F, 0F, 6F, 6F, 3F, new Dilation(0.35F)), ModelTransform.of(0F, -8F, -4F, -0.1745F, 0F, 0F));
		ModelPartData cloak = body.addChild("cloak", ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r2 = cloak.addChild("cube_r2", ModelPartBuilder.create().uv(24, 91).cuboid(-5F, -1F, 2F, 10F, 16F, 1F, new Dilation(0.29F)), ModelTransform.of(0F, 2F, 0F, 0.1745F, 0F, 0F));
		ModelPartData openHood = cloak.addChild("openHood", ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r3 = openHood.addChild("cube_r3", ModelPartBuilder.create().uv(0, 80).cuboid(-5F, -4F, 0F, 10F, 4F, 7F, new Dilation(0.3F)), ModelTransform.of(0F, 2F, 0F, 0.1745F, 0F, 0F));
		ModelPartData garb = body.addChild("garb", ModelPartBuilder.create().uv(0, 91).cuboid(-4F, -12F, -2F, 8F, 11F, 4F, new Dilation(0.3F)), ModelTransform.pivot(0F, 12F, 0F));
		ModelPartData belt = body.addChild("belt", ModelPartBuilder.create().uv(41, 80).cuboid(-3F, -3F, -2F, 6F, 3F, 1F, new Dilation(0.2F)), ModelTransform.pivot(0F, 12F, 0F));
		ModelPartData backCover = garb.addChild("backCover", ModelPartBuilder.create().uv(16, 108).cuboid(-4F, 0F, 0F, 8F, 8F, 1F, new Dilation(0.29F)), ModelTransform.of(0F, -1F, 1F, 0.2618F, 0F, 0F));
		ModelPartData rightCover = garb.addChild("rightCover", ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r4 = rightCover.addChild("cube_r4", ModelPartBuilder.create().uv(50, 110).cuboid(-1F, -1F, -2F, 1F, 8F, 4F, new Dilation(0.29F)), ModelTransform.of(-3F, 0F, 0F, 0F, 0F, 0.2618F));
		ModelPartData leftCover = garb.addChild("leftCover", ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r5 = leftCover.addChild("cube_r5", ModelPartBuilder.create().uv(48, 64).cuboid(0F, -1F, -2F, 1F, 8F, 4F, new Dilation(0.29F)), ModelTransform.of(3F, 0F, 0F, 0F, 0F, -0.2618F));
		ModelPartData rightSleeve = rightArm.addChild("rightSleeve", ModelPartBuilder.create(), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData cube_r6 = rightSleeve.addChild("cube_r6", ModelPartBuilder.create().uv(0, 106).cuboid(-3F, -2F, -2F, 4F, 10F, 4F, new Dilation(0.29F)), ModelTransform.of(0F, 0F, 0F, 0F, 0F, 0F));
		ModelPartData leftSleeve = leftArm.addChild("leftSleeve", ModelPartBuilder.create(), ModelTransform.pivot(2F, 0F, 0F));
		ModelPartData cube_r7 = leftSleeve.addChild("cube_r7", ModelPartBuilder.create().uv(32, 64).cuboid(-1F, -2F, -2F, 4F, 10F, 4F, new Dilation(0.29F)), ModelTransform.of(-2F, 0F, 0F, 0F, 0F, 0F));
		ModelPartData rightShoe = rightLeg.addChild("rightShoe", ModelPartBuilder.create().uv(34, 110).cuboid(-2F, 7F, -2F, 4F, 5F, 4F, new Dilation(0.2F)), ModelTransform.pivot(0F, 0F, 0F));
		ModelPartData leftShoe = leftLeg.addChild("leftShoe", ModelPartBuilder.create().uv(46, 90).cuboid(-2F, 7F, -2F, 4F, 5F, 4F, new Dilation(0.199F)), ModelTransform.pivot(0.2F, 0F, 0F));

		return TexturedModelData.of(data, 64, 128);
	}

	@Override
	public void setAngles(T entity, float f, float g, float h, float i, float j) {
		super.setAngles(entity, f, g, h, i, j);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}

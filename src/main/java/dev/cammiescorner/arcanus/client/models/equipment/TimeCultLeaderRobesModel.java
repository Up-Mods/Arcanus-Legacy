package dev.cammiescorner.arcanus.client.models.equipment;

import dev.cammiescorner.arcanus.Arcanus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class TimeCultLeaderRobesModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Arcanus.id("time_cultist_leader_robes"), "main");
	public final ModelPart skull;
	public final ModelPart robe;
	public final ModelPart leftLegArmour;
	public final ModelPart rightLegArmour;
	public final ModelPart leftShoe;
	public final ModelPart rightShoe;

	public TimeCultLeaderRobesModel(ModelPart root) {
		super(root, RenderLayer::getArmorCutoutNoCull);
		skull = head.getChild("skull");
		robe = body.getChild("robe");
		leftLegArmour = leftLeg.getChild("leftLegArmour");
		rightLegArmour = rightLeg.getChild("rightLegArmour");
		leftShoe = leftLeg.getChild("leftShoe");
		rightShoe = rightLeg.getChild("rightShoe");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData head = data.getRoot().getChild(EntityModelPartNames.HEAD);
		ModelPartData body = data.getRoot().getChild(EntityModelPartNames.BODY);
		ModelPartData rightLeg = data.getRoot().getChild(EntityModelPartNames.RIGHT_LEG);
		ModelPartData leftLeg = data.getRoot().getChild(EntityModelPartNames.LEFT_LEG);

		ModelPartData skull = head.addChild("skull", ModelPartBuilder.create().uv(72, 93).cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 7.0F, 8.0F, new Dilation(0.4F)).uv(96, 100).cuboid(-4.0F, -7.1F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(0.35F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r1 = skull.addChild("cube_r1", ModelPartBuilder.create().uv(106, 73).cuboid(-9.0F, -12.0F, -2.0F, 7.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6545F, 0.7854F));
		ModelPartData cube_r2 = skull.addChild("cube_r2", ModelPartBuilder.create().uv(78, 47).cuboid(2.0F, -12.0F, -2.0F, 7.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.6545F, -0.7854F));
		ModelPartData robe = body.addChild("robe", ModelPartBuilder.create().uv(46, 101).cuboid(-4.0F, 1.0F, -2.0F, 8.0F, 13.0F, 4.0F, new Dilation(0.9F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData cube_r3 = robe.addChild("cube_r3", ModelPartBuilder.create().uv(79, 55).cuboid(-5.0F, -12.0F, -5.0F, 10.0F, 9.0F, 9.0F, new Dilation(0.5F)), ModelTransform.of(0.0F, 2.0F, 2.0F, 0.1309F, 0.0F, 0.0F));
		ModelPartData cape = robe.addChild("cape", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 2.0F));
		ModelPartData cube_r4 = cape.addChild("cube_r4", ModelPartBuilder.create().uv(70, 108).cuboid(-4.0F, -2.0F, 0.0F, 8.0F, 18.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
		ModelPartData cube_r5 = cape.addChild("cube_r5", ModelPartBuilder.create().uv(46, 81).cuboid(2.0F, 3.0F, -6.0F, 9.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.0436F, -0.0873F));
		ModelPartData cube_r6 = cape.addChild("cube_r6", ModelPartBuilder.create().uv(0, 64).cuboid(1.0F, -2.0F, -7.0F, 11.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.0436F, -0.2618F));
		ModelPartData cube_r7 = cape.addChild("cube_r7", ModelPartBuilder.create().uv(80, 73).cuboid(-11.0F, 3.0F, -6.0F, 9.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.0873F));
		ModelPartData cube_r8 = cape.addChild("cube_r8", ModelPartBuilder.create().uv(46, 64).cuboid(-12.0F, -2.0F, -7.0F, 11.0F, 7.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.2618F));
		ModelPartData rightLegArmour = rightLeg.addChild("rightLegArmour", ModelPartBuilder.create().uv(110, 89).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.26F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData rightShoe = rightLeg.addChild("rightShoe", ModelPartBuilder.create().uv(106, 112).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.399F)).uv(46, 66).cuboid(-2.0F, 11.0F, -3.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.399F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData leftLegArmour = leftLeg.addChild("leftLegArmour", ModelPartBuilder.create().uv(108, 47).cuboid(-1.8F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new Dilation(0.26F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		ModelPartData leftShoe = leftLeg.addChild("leftShoe", ModelPartBuilder.create().uv(90, 112).cuboid(-1.8F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.4F)).uv(46, 64).cuboid(-1.8F, 11.0F, -3.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(data, 128, 128);
	}
}

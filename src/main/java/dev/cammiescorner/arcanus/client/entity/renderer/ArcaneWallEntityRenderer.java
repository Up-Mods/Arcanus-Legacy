package dev.cammiescorner.arcanus.client.entity.renderer;

import dev.cammiescorner.arcanus.common.entities.ArcaneWallEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class ArcaneWallEntityRenderer extends EntityRenderer<ArcaneWallEntity> {
	public ArcaneWallEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(ArcaneWallEntity entity) {
		return null;
	}
}

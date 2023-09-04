package dev.cammiescorner.arcanus.client.renderer.entity;

import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MagicMissileEntityRenderer extends EntityRenderer<MagicMissileEntity> {
    public MagicMissileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(MagicMissileEntity entity) {
        return null;
    }
}

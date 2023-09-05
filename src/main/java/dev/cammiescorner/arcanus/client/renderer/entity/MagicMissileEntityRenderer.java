package dev.cammiescorner.arcanus.client.renderer.entity;

import dev.cammiescorner.arcanus.entity.MagicMissileEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MagicMissileEntityRenderer extends EntityRenderer<MagicMissileEntity> {
    public MagicMissileEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicMissileEntity entity) {
        return null;
    }
}

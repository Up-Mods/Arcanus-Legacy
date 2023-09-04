package dev.cammiescorner.arcanus.core.mixin;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.core.util.CanBeDiscombobulated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//TODO port to CCA entity component
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements CanBeDiscombobulated {

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new UnsupportedOperationException();
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    public Vec3d invertInput(Vec3d movementInput) {
        if (this.isDiscombobulated())
            movementInput = movementInput.multiply(-1, 1, -1);

        return movementInput;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (isDiscombobulated())
            setDiscombobulatedTimer(getDiscombobulatedTimer() - 1);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound tag, CallbackInfo info) {
        NbtCompound rootTag = tag.getCompound(Arcanus.MOD_ID);

        dataTracker.set(Arcanus.DataTrackers.DISCOMBOBULATED_TIMER, rootTag.getInt("DiscombobulatedTimer"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound tag, CallbackInfo info) {
        NbtCompound rootTag = new NbtCompound();

        tag.put(Arcanus.MOD_ID, rootTag);
        rootTag.putInt("DiscombobulatedTimer", dataTracker.get(Arcanus.DataTrackers.DISCOMBOBULATED_TIMER));
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initTracker(CallbackInfo info) {
        dataTracker.startTracking(Arcanus.DataTrackers.DISCOMBOBULATED_TIMER, 0);
    }

    @Override
    public int getDiscombobulatedTimer() {
        return dataTracker.get(Arcanus.DataTrackers.DISCOMBOBULATED_TIMER);
    }

    @Override
    public void setDiscombobulatedTimer(int time) {
        dataTracker.set(Arcanus.DataTrackers.DISCOMBOBULATED_TIMER, time);
    }
}

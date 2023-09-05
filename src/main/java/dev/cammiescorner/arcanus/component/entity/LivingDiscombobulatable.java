package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.component.base.CanBeDiscombobulated;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class LivingDiscombobulatable implements CanBeDiscombobulated, ServerTickingComponent, AutoSyncedComponent {

    private final LivingEntity entity;

    private int discombobulatedTimer;
    private boolean isDiscombobulated;

    public LivingDiscombobulatable(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean isDiscombobulated() {
        return isDiscombobulated;
    }

    @Override
    public int getDiscombobulatedTimer() {
        return discombobulatedTimer;
    }

    @Override
    public void setDiscombobulatedTimer(int time) {
        this.discombobulatedTimer = Math.max(time, 0);
        this.isDiscombobulated = time > 0;
    }

    @Override
    public void serverTick() {
        if(getDiscombobulatedTimer() > 0) {
            discombobulatedTimer--;

            if(getDiscombobulatedTimer() == 0) {
                entity.syncComponent(ArcanusComponents.CAN_BE_DISCOMBOBULATED);
            }
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.discombobulatedTimer = tag.getInt("discombobulatedTimer");
        this.isDiscombobulated = getDiscombobulatedTimer() > 0;
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putInt("discombobulatedTimer", getDiscombobulatedTimer());
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer recipient) {
        buf.writeBoolean(this.isDiscombobulated());
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        this.isDiscombobulated = buf.readBoolean();
    }
}

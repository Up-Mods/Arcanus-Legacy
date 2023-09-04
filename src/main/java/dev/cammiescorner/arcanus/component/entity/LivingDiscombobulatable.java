package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.component.base.CanBeDiscombobulated;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

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
    public void readFromNbt(NbtCompound tag) {
        this.discombobulatedTimer = tag.getInt("discombobulatedTimer");
        this.isDiscombobulated = getDiscombobulatedTimer() > 0;
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("discombobulatedTimer", getDiscombobulatedTimer());
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(this.isDiscombobulated());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.isDiscombobulated = buf.readBoolean();
    }
}

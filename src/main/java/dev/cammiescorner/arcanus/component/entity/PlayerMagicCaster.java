package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class PlayerMagicCaster implements MagicCaster, CopyableComponent<PlayerMagicCaster>, AutoSyncedComponent {

    private final PlayerEntity player;
    private int mana;
    private int burnout;
    private long lastCastTime;

    public PlayerMagicCaster(PlayerEntity player) {
        this.player = player;
        this.mana = getMaxManaDirect();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        mana = tag.getInt("mana");
        burnout = tag.getInt("burnout");
        lastCastTime = tag.getLong("lastCastTime");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("mana", mana);
        tag.putInt("burnout", burnout);
        tag.putLong("lastCastTime", lastCastTime);
    }

    @Override
    public void copyFrom(PlayerMagicCaster other) {
        NbtCompound tag = new NbtCompound();
        other.writeToNbt(tag);
        this.readFromNbt(tag);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public int getMaxMana() {
        return Math.max(getMaxManaDirect() - ArcanusEntityAttributes.getManaLock(this.player), 0);
    }

    @Override
    public void setMana(int amount) {
        this.mana = MathHelper.clamp(amount, 0, Math.max(getMaxMana() - getBurnout(), 0));
    }

    @Override
    public int getBurnout() {
        return burnout;
    }

    @Override
    public void setBurnout(int amount) {
        this.burnout = MathHelper.clamp(amount, 0, getMaxBurnout());
    }

    @Override
    public int getMaxBurnout() {
        return getMaxMana();
    }

    @Override
    public long getLastCastTime() {
        return lastCastTime;
    }

    @Override
    public void setLastCastTime(long lastCastTime) {
        this.lastCastTime = lastCastTime;
    }
}

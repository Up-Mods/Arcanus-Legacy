package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.ArcanusComponents;
import dev.cammiescorner.arcanus.component.base.MagicCaster;
import dev.cammiescorner.arcanus.event.SpellEvents;
import dev.cammiescorner.arcanus.registry.ArcanusEntityAttributes;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.ArcanusConfig;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.base.api.util.TriState;

public class PlayerMagicCaster implements MagicCaster, CopyableComponent<PlayerMagicCaster>, AutoSyncedComponent, ServerTickingComponent {

    private final Player player;
    private int mana;
    private int burnout;
    private long lastCastTime;
    private Spell activeSpell;
    private int activeSpellTimer;

    public PlayerMagicCaster(Player player) {
        this.player = player;
        this.mana = getMaxManaDirect();
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        mana = tag.getInt("mana");
        burnout = tag.getInt("burnout");
        lastCastTime = tag.getLong("lastCastTime");
        if(tag.contains("activeSpell", Tag.TAG_STRING)) {
            activeSpell = Arcanus.SPELL.get(new ResourceLocation(tag.getString("activeSpell")));
            if(activeSpell == null) {
                Arcanus.LOGGER.error("Received unknown spell id {} while reading component data for player {}", tag.getString("activeSpell"), player.getGameProfile().getName());
                activeSpellTimer = 0;
            }
            else {
                activeSpellTimer = tag.getInt("activeSpellTimer");
            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putInt("mana", mana);
        tag.putInt("burnout", burnout);
        tag.putLong("lastCastTime", lastCastTime);
        if(activeSpell != null) {
            tag.putString("activeSpell", Arcanus.SPELL.getKey(activeSpell).toString());
            tag.putInt("activeSpellTimer", activeSpellTimer);
        }
    }

    @Override
    public void copyFrom(PlayerMagicCaster other) {
        CompoundTag tag = new CompoundTag();
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
        this.mana = Mth.clamp(amount, 0, Math.max(getMaxMana() - getBurnout(), 0));
    }

    @Override
    public int getBurnout() {
        return burnout;
    }

    @Override
    public void setBurnout(int amount) {
        this.burnout = Mth.clamp(amount, 0, getMaxBurnout());
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

    @Override
    public LivingEntity asEntity() {
        return player;
    }

    @Override
    public boolean cast(Spell spell) {
        if(SpellEvents.TRY_CAST.invoker().tryCast(this, spell) == TriState.FALSE) {
            return false;
        }

        SpellEvents.ON_CAST.invoker().onCast(this, spell);
        spell.onCast(this);
        if(spell.getMaxSpellTime() > 0) {
            setActiveSpellTime(spell, spell.getMaxSpellTime());
        }
        return true;
    }

    @Override
    public void setActiveSpellTime(@Nullable Spell spell, int remainingTicks) {
        activeSpell = spell;
        activeSpellTimer = activeSpell != null ? remainingTicks : 0;
    }

    @Override
    public Spell getActiveSpell() {
        return activeSpell;
    }

    @Override
    public void serverTick() {
        Level level = player.level();
        if (activeSpell != null) {
            activeSpell.onActiveTick(level, this, activeSpellTimer);

            if (activeSpellTimer == 0) {
                activeSpell = null;
                asEntity().syncComponent(ArcanusComponents.MAGIC_CASTER);
            }
            else {
                activeSpellTimer--;
            }
        }

        if (level.getGameTime() - this.getLastCastTime() >= 20) {
            boolean dirty = false;
            int manaCooldown = (int) Math.round(ArcanusConfig.baseManaCooldown * ArcanusEntityAttributes.getManaRegen(player));
            int burnoutCooldown = (int) Math.round(ArcanusConfig.baseBurnoutCooldown * ArcanusEntityAttributes.getBurnoutRegen(player));

            if (manaCooldown != 0 && this.getMana() < this.getMaxMana() - this.getBurnout() && level.getGameTime() % manaCooldown == 0) {
                this.addMana(1);
                dirty = true;
            }

            if (burnoutCooldown != 0 && this.getBurnout() > 0 && level.getGameTime() % burnoutCooldown == 0) {
                this.addBurnout(-1);
                player.causeFoodExhaustion(5F);
                dirty = true;
            }

            if (dirty) {
                player.syncComponent(ArcanusComponents.MAGIC_CASTER);
            }
        }
    }
}

package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.SpellMemory;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class PlayerSpellMemory implements SpellMemory, CopyableComponent<PlayerSpellMemory> {

    private final Player player;
    private final Set<Spell> knownSpells = new HashSet<>();
    private final Set<Spell> knownSpellsView = Collections.unmodifiableSet(knownSpells);

    public PlayerSpellMemory(Player player) {
        this.player = player;
    }

    @Override
    public Set<Spell> getKnownSpells() {
        return knownSpellsView;
    }

    @Override
    public boolean unlockSpell(@Nullable Spell spell) {
        if(spell == null) {
            return false;
        }
        return knownSpells.add(spell);
    }

    @Override
    public boolean removeSpell(@Nullable Spell spell) {
        if(spell == null) {
            return false;
        }
        return knownSpells.remove(spell);
    }

    @Override
    public void clear() {
        knownSpells.clear();
    }

    @Override
    public void copyFrom(PlayerSpellMemory other) {
        knownSpells.clear();
        knownSpells.addAll(other.knownSpells);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        ListTag spells = tag.getList("spells", Tag.TAG_STRING);
        spells.forEach(value -> Arcanus.SPELL.getOptional(new ResourceLocation(value.getAsString())).ifPresentOrElse(knownSpells::add, () -> Arcanus.LOGGER.error("Spell memory for {} unable to deserialize spell: {}", this.player.getGameProfile().getName(), value.getAsString())));
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        ListTag spells = new ListTag();
        knownSpells.forEach(spell -> spells.add(StringTag.valueOf(Arcanus.SPELL.getKey(spell).toString())));
        tag.put("spells", spells);
    }
}

package dev.cammiescorner.arcanus.component.entity;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.component.base.SpellMemory;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerSpellMemory implements SpellMemory, CopyableComponent<PlayerSpellMemory> {

    private final PlayerEntity player;
    private final Set<Spell> knownSpells = new HashSet<>();
    private final Set<Spell> knownSpellsView = Collections.unmodifiableSet(knownSpells);

    public PlayerSpellMemory(PlayerEntity player) {
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
    public void readFromNbt(NbtCompound tag) {
        NbtList spells = tag.getList("spells", NbtElement.STRING_TYPE);
        spells.forEach(value -> Arcanus.SPELL.getOrEmpty(new Identifier(value.asString())).ifPresentOrElse(knownSpells::add, () -> Arcanus.LOGGER.error("Spell memory for {} unable to deserialize spell: {}", this.player.getGameProfile().getName(), value.asString())));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList spells = new NbtList();
        knownSpells.forEach(spell -> spells.add(NbtString.of(Arcanus.SPELL.getId(spell).toString())));
        tag.put("spells", spells);
    }
}

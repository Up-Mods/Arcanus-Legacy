package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.actions.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArcanusAltarActions {
	//-----Spell Map-----//
	public static final Map<AltarAction, Identifier> ALTAR_ACTIONS = new LinkedHashMap<>();

	public static final AltarAction EMPTY = create("empty", new EmptyAltarAction());
	public static final ItemAltarAction CRAFT_ITEM = create("craft_item", new ItemAltarAction());
	public static final SummonAltarAction SUMMON_ENTITY = create("summon_entity", new SummonAltarAction());
	public static final RestoreAuraAltarAction RESTORE_AURA = create("restore_aura", new RestoreAuraAltarAction());

	//-----Registry-----//
	public static void register() {
		ALTAR_ACTIONS.forEach((spell, id) -> Registry.register(Arcanus.ALTAR_ACTIONS, id, spell));
	}

	private static <T extends AltarAction> T create(String name, T spell) {
		ALTAR_ACTIONS.put(spell, Arcanus.id(name));
		return spell;
	}
}

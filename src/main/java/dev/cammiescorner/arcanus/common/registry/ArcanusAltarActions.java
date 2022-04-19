package dev.cammiescorner.arcanus.common.registry;

import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.api.ArcanusHelper;
import dev.cammiescorner.arcanus.api.actions.AltarAction;
import dev.cammiescorner.arcanus.api.actions.ItemAltarAction;
import dev.cammiescorner.arcanus.api.actions.PlayerAltarAction;
import dev.cammiescorner.arcanus.api.actions.SummonAltarAction;
import dev.cammiescorner.arcanus.common.components.entity.AuraComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArcanusAltarActions {
	//-----Spell Map-----//
	public static final Map<AltarAction, Identifier> ALTAR_ACTIONS = new LinkedHashMap<>();

	public static final AltarAction EMPTY = create("empty", AltarAction.EMPTY);
	public static final ItemAltarAction CRAFT_ITEM = create("craft_item", new ItemAltarAction());
	public static final SummonAltarAction SUMMON_ENTITY = create("summon_entity", new SummonAltarAction());
	public static final PlayerAltarAction RESTORE_AURA = create("restore_aura", (world, player, altar) -> ArcanusHelper.setAura(player, AuraComponent.MAX_AURA));

	//-----Registry-----//
	public static void register() {
		ALTAR_ACTIONS.forEach((spell, id) -> Registry.register(Arcanus.ALTAR_ACTIONS, id, spell));
	}

	private static <T extends AltarAction> T create(String name, T spell) {
		ALTAR_ACTIONS.put(spell, Arcanus.id(name));
		return spell;
	}
}

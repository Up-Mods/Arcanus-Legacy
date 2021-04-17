package dev.cammiescorner.arcanus;

import dev.cammiescorner.arcanus.common.packets.CastSpellMessage;
import dev.cammiescorner.arcanus.common.spells.Spell;
import dev.cammiescorner.arcanus.core.EventHandler;
import dev.cammiescorner.arcanus.core.registry.ModItems;
import dev.cammiescorner.arcanus.core.registry.ModSpells;
import dev.cammiescorner.arcanus.core.util.Pattern;
import dev.cammiescorner.arcanus.core.util.SpellBooks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Arcanus implements ModInitializer
{
	//-----Custom Registries-----//
	public static final Registry<Spell> SPELL = createRegistry("spell", Spell.class);

	public static final String MOD_ID = "arcanus";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "general")).appendItems(list ->
	{
		Registry.ITEM.forEach(item -> item.appendStacks(Arcanus.ITEM_GROUP, (DefaultedList<ItemStack>) list));
		Arcanus.SPELL.forEach(spell -> list.add(SpellBooks.getBookFromSpell(spell)));
	}).icon(() -> new ItemStack(ModItems.WAND)).build();

	@Override
	public void onInitialize()
	{
		ServerPlayNetworking.registerGlobalReceiver(CastSpellMessage.ID, CastSpellMessage::handle);

		ModItems.register();
		ModSpells.register();

		EventHandler.commonEvents();

		LOGGER.info("A new magical force awakens...");
	}

	@SuppressWarnings("unchecked")
	private static <T> Registry<T> createRegistry(String name, Class<?> clazz)
	{
		Registry<?> registry = FabricRegistryBuilder.createSimple(clazz, new Identifier(MOD_ID, name)).buildAndRegister();
		return (Registry<T>) registry;
	}

	public static MutableText getSpellInputs(List<Pattern> pattern, int index)
	{
		return index >= pattern.size() || pattern.get(index) == null ? new LiteralText("?").formatted(Formatting.GRAY, Formatting.UNDERLINE) :
				new LiteralText(pattern.get(index).getSymbol()).formatted(Formatting.GREEN);
	}
}
